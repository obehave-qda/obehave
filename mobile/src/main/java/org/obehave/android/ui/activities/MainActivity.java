package org.obehave.android.ui.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.common.eventbus.Subscribe;
import org.joda.time.LocalDateTime;
import org.obehave.android.R;
import org.obehave.android.application.MyApplication;
import org.obehave.android.ui.adapters.SectionsPagerAdapter;
import org.obehave.android.ui.events.*;
import org.obehave.android.ui.exceptions.UiException;
import org.obehave.android.ui.fragments.ActionFragment;
import org.obehave.android.ui.fragments.SubjectFragment;
import org.obehave.android.util.ErrorDialog;
import org.obehave.events.EventBusHolder;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Action;
import org.obehave.model.Observation;
import org.obehave.model.Subject;
import org.obehave.model.modifier.Modifier;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    // constants
    private static final int REQUEST_CODE_LOAD_STUDY = 1;
    private static final int REQUEST_CREATE_OBSERVATION = 2;
    private static final int CODING_FRAGMENT_POSITION = 2;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final long INTERVAL_TIME = 200;


    // ui members
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ActionBar mActionBar;

    protected MyApplication app;


    private Timer timer;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (app.getObservation() != null && app.getObservation().getDateTime() != null)
                EventBusHolder.post(new TimerTaskEvent(app.getObservation().getDateTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_main);

        app = (MyApplication) getApplication();

        mActionBar = getActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mActionBar.setSelectedNavigationItem(position);
            }
        });

        onCreatePageAdapter();

        if (savedInstanceState == null) {
            openStudyChooserActivity();
            // init first codingFragment!
            mSectionsPagerAdapter.changeCodingFragment(SubjectFragment.newInstance(CODING_FRAGMENT_POSITION));
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
        EventBusHolder.register(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBusHolder.unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (getActionBar().getSelectedTab().getPosition() == CODING_FRAGMENT_POSITION) {
            if (!mSectionsPagerAdapter.back()) {
                finish();
            }
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_open_coding_schema, menu);
        return true;
    }

    @Subscribe
    public void onItemSelected(ItemSelectedEvent event) {
        try {
            if (event.getItem() instanceof Subject) {
                app.getCodingState().setStartTime(LocalDateTime.now()).setSubject((Subject) event.getItem());
                mSectionsPagerAdapter.changeCodingFragment(ActionFragment.newInstance(CODING_FRAGMENT_POSITION));
                return;
            }

            if (event.getItem() instanceof Action) {
                app.getCodingState().setAction((Action) event.getItem());
                if (((Action) event.getItem()).getModifierFactory() != null) {
                    mSectionsPagerAdapter.changeCodingFragment((Action) event.getItem());
                    return;
                }
            }

            if (event.getItem() instanceof Modifier) {
                app.getCodingState().setModifier((Modifier) event.getItem());
            }

            app.createCoding();
            app.resetCodingState();
            resetView();
        } catch (UiException e) {
            ErrorDialog ed = new ErrorDialog(e, this);
            ed.invoke();
            Log.d(LOG_TAG, e.getMessage());
            Log.d(LOG_TAG, e.getInnerException().getMessage());
        }
    }

    private void resetView() {
        mSectionsPagerAdapter.clearHistory();
        mSectionsPagerAdapter.changeCodingFragment(SubjectFragment.newInstance(CODING_FRAGMENT_POSITION));
        mViewPager.setCurrentItem(1);
    }

    @Subscribe
    public void onTimerStartEvent(TimerStartEvent event) {
        Log.d(LOG_TAG, "onTimerStartEvent");
        startCreateObservationActivityForResult();

    }


    @Subscribe
    public void onTimerStopEvent(TimerStopEvent event) {
        Log.d(LOG_TAG, "onTimerStopEvent");
        stopTimer();
    }

    public void stopTimer() {
        if (timer != null) {

            timer.cancel();
            timer = null;
        }
    }

    private void startCreateObservationActivityForResult() {
        Intent intent = new Intent(this, ObservationActivity.class);
        startActivityForResult(intent, REQUEST_CREATE_OBSERVATION);
    }

    @Subscribe
    public void onNodeSelected(NodeSelectedEvent event) {
        if (event.getNodeType() == NodeSelectedEvent.NodeType.ACTION) {
            mSectionsPagerAdapter.changeCodingFragment(ActionFragment.newInstance(CODING_FRAGMENT_POSITION, event.getNode()));
        }
    }

    public void openStudyChooserActivity() {
        Intent intent = new Intent(this, OpenStudyActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOAD_STUDY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_LOAD_STUDY) {
            chooseStudyActivityResult(resultCode, data);
        } else if (requestCode == REQUEST_CREATE_OBSERVATION) {
            createObservationActivityResult(resultCode, data);
        }
    }

    private void chooseStudyActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            try {
                app.loadStudy(data.getStringExtra(OpenStudyActivity.RESULT_OBJECT_FILENAME));
            } catch (UiException e) {
                ErrorDialog ed = new ErrorDialog(e, this);
                ed.invoke();
                // if study not loaded open dialog again.
                openStudyChooserActivity();
            }
            Log.d(LOG_TAG, "RESULT_OK");
        } else {
            openStudyChooserActivity();
            Log.d(LOG_TAG, "RESULT_NOK");
        }
    }

    private void createObservationActivityResult(int resultCode, Intent data) {
        Log.d(LOG_TAG, "createObservationActivityResult");
        if (resultCode == Activity.RESULT_OK) {
            try {

                Bundle bundle = data.getExtras();
                app.setObservation((Observation) bundle.getSerializable(ObservationActivity.RESULT_OBSERVATION));
                onObservationCreated(app.getObservation());
            } catch (ServiceException e) {
                ErrorDialog ed = new ErrorDialog(e, this);
                ed.invoke();
                openStudyChooserActivity();
            }

            Log.d(LOG_TAG, "RESULT_OK");
        } else {
            openStudyChooserActivity();
            Log.d(LOG_TAG, "RESULT_NOK");
        }
    }

    private void onObservationCreated(Observation observation) throws ServiceException {

        app.getObservationService().save(observation);
        mSectionsPagerAdapter.clearHistory();
        mSectionsPagerAdapter.changeCodingFragment(SubjectFragment.newInstance(CODING_FRAGMENT_POSITION));
        startTimer();
    }

    private void startTimer() {
        // starttimer
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                mHandler.obtainMessage(1).sendToTarget();
            }
        }, 0, INTERVAL_TIME);
    }


    private void onCreatePageAdapter() {
        Log.d(LOG_TAG, "onCreatePageAdapter");
        // init sections pager
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, mActionBar, mViewPager, getSupportFragmentManager());
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            String title = "";
            Integer resourceTitle = mSectionsPagerAdapter.getPageTitleResource(i);
            if (resourceTitle != null) {
                title = getString(resourceTitle);
            }

            mActionBar.addTab(
                    mActionBar.newTab()
                            .setText(title)
                            .setTabListener(this));
        }

        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
