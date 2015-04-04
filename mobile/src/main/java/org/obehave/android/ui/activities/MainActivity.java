package org.obehave.android.ui.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.common.eventbus.Subscribe;
import org.obehave.android.R;
import org.obehave.android.application.CodingState;
import org.obehave.android.application.MyApplication;
import org.obehave.android.database.DataHolder;
import org.obehave.android.ui.adapters.SectionsPagerAdapter;
import org.obehave.android.ui.events.ItemSelectedEvent;
import org.obehave.android.ui.events.NodeSelectedEvent;
import org.obehave.android.ui.events.TimerStartEvent;
import org.obehave.android.ui.events.TimerStopEvent;
import org.obehave.android.ui.exceptions.UiException;
import org.obehave.android.ui.fragments.*;
import org.obehave.android.util.DateTimeHelper;
import org.obehave.android.util.ErrorDialog;
import org.obehave.events.EventBusHolder;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Action;
import org.obehave.model.Observation;
import org.obehave.model.Subject;
import org.obehave.model.modifier.Modifier;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.service.CodingService;
import org.obehave.service.ObservationService;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    // constants
    private static final int REQUEST_CODE_LOAD_STUDY = 1;
    private static final int REQUEST_CREATE_OBSERVATION = 2;
    private static final int CODING_FRAGMENT_POSITION = 2;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // ui members
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ActionBar mActionBar;

    // services
    private ObservationService observationService;
    private CodingService codingService;
    /**
     * Stores the current coding state
     */
    private CodingState codingState;
    private Observation observation;

    @Subscribe
    public void onItemSelected(ItemSelectedEvent event) {
        if (event.getItem() instanceof Subject) {
            codingState.setStartTime();
            codingState.setSubject((Subject)event.getItem());
            mSectionsPagerAdapter.switchToNextCodingFragment(ActionFragment.newInstance(CODING_FRAGMENT_POSITION));
        } else if (event.getItem() instanceof Action) {
            codingState.setAction((Action) event.getItem());
            if(((Action) event.getItem()).getModifierFactory() != null) {
                displayModifierFragmentByGivenAction((Action) event.getItem());
            }
            else {
                createCoding();
                resetView();
            }
        } else if (event.getItem() instanceof Modifier){
            codingState.setModifier((Modifier) event.getItem());
            createCoding();
            resetView();
        }
    }

    private void createCoding(){
        try {
            codingService.startCoding(codingState.getSubject(),
                    codingState.getAction(),
                    codingState.getModifier().getBuildString(),
                    DateTimeHelper.diffMs(observation.getDateTime(), codingState.getStartTime()));

        } catch (ServiceException e) {
            ErrorDialog ed = new ErrorDialog(e, this);
            ed.invoke();
        }
    }

    private void displayModifierFragmentByGivenAction(Action action) {
        Fragment fragment = null;

        if (action.getModifierFactory().getType() == ModifierFactory.Type.SUBJECT_MODIFIER_FACTORY) {
            fragment = SubjectModifierFragment.newInstance(CODING_FRAGMENT_POSITION, action);
        } else if (action.getModifierFactory().getType() == ModifierFactory.Type.DECIMAL_RANGE_MODIFIER_FACTORY) {
            fragment = DecimalRangeModifierFragment.newInstance(CODING_FRAGMENT_POSITION, action);
        } else if (action.getModifierFactory().getType() == ModifierFactory.Type.ENUMERATION_MODIFIER_FACTORY) {
            fragment = EnumerationModifierFragment.newInstance(CODING_FRAGMENT_POSITION, action);
        }

        mSectionsPagerAdapter.switchToNextCodingFragment(fragment);
    }

    private void resetView(){
        mSectionsPagerAdapter.switchToNextCodingFragment(SubjectFragment.newInstance(CODING_FRAGMENT_POSITION));
        mViewPager.setCurrentItem(1);
    }

    @Subscribe
    public void onTimerStartEvent(TimerStartEvent event) {
        Log.d(LOG_TAG, "onTimerStartEvent");
        startCreateObservationActivityForResult();
        MyApplication.startTimer();
    }

    private void startCreateObservationActivityForResult() {
        Intent intent = new Intent(this, ObservationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ObservationActivity.ARG_STUDY, DataHolder.getStudy());
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CREATE_OBSERVATION);
    }

    @Subscribe
    public void onTimerStopEvent(TimerStopEvent event) {
        Log.d(LOG_TAG, "onTimerStopEvent");
        MyApplication.stopTimer();
    }

    @Subscribe
    public void onNodeSelected(NodeSelectedEvent event) {
        if (event.getNodeType() == NodeSelectedEvent.NodeType.SUBJECT) {
            mSectionsPagerAdapter.switchToNextCodingFragment(SubjectFragment.newInstance(CODING_FRAGMENT_POSITION, event.getNode()));
        } else if (event.getNodeType() == NodeSelectedEvent.NodeType.ACTION) {
            mSectionsPagerAdapter.switchToNextCodingFragment(ActionFragment.newInstance(CODING_FRAGMENT_POSITION, event.getNode()));
        }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "create");
        //if(savedInstanceState == null

        setContentView(R.layout.activity_main);

        mActionBar = getActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mActionBar.setSelectedNavigationItem(position);
            }
        });

        if (!DataHolder.isStudyLoaded()) {
            openStudyChooserActivity();
        }
        else {
            initPageAdapter();
        }
        codingState = new CodingState();
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
        }
        else if(requestCode == REQUEST_CREATE_OBSERVATION){
            createObservationActivityResult(resultCode, data);
        }
    }

    private void chooseStudyActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            try {
                String filename = data.getStringExtra(OpenStudyActivity.RESULT_OBJECT_FILENAME);
                DataHolder.loadStudy(filename);
                initPageAdapter();


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
                observation = (Observation) bundle.getSerializable(ObservationActivity.RESULT_OBSERVATION);
                initializeServices();
                observationService.save(observation);
                Log.d(LOG_TAG, observation.toString());
            }  catch (ServiceException e) {
                ErrorDialog ed = new ErrorDialog(e, this);
                ed.invoke();
                openStudyChooserActivity();
                e.printStackTrace();
            }

            Log.d(LOG_TAG, "RESULT_OK");
        } else {
            openStudyChooserActivity();
            Log.d(LOG_TAG, "RESULT_NOK");
        }
    }

    private void initializeServices() {
        observationService = DataHolder.getStudy().getObservationService();
        codingService = DataHolder.getStudy().getCodingServiceBuilder().build(observation);
    }

    private void initPageAdapter() {
        // init sections pager
        mSectionsPagerAdapter = new SectionsPagerAdapter(mActionBar, mViewPager, getSupportFragmentManager(), SubjectFragment.newInstance(CODING_FRAGMENT_POSITION));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_open_coding_schema, menu);
        return true;
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
        /* TODO: Check if timer is stopped  */
        MyApplication.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
        EventBusHolder.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
        EventBusHolder.unregister(this);
    }

}
