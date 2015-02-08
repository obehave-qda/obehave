package org.obehave.android.ui.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.common.eventbus.Subscribe;
import org.obehave.android.R;
import org.obehave.android.ui.adapters.SectionsPagerAdapter;
import org.obehave.android.ui.events.ActionSelectedEvent;
import org.obehave.android.ui.events.SubjectSelectedEvent;
import org.obehave.android.ui.fragments.ActionFragment;
import org.obehave.android.ui.fragments.SubjectModifierFragment;
import org.obehave.android.ui.util.AppState;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Action;
import org.obehave.model.modifier.SubjectModifierFactory;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;



    @Subscribe
    public  void onSubjectSelected(SubjectSelectedEvent event){
        Log.d(LOG_TAG, "onSubjectSelected");
        Log.d(LOG_TAG, event.getSubject().getDisplayString());
        mSectionsPagerAdapter.setCodingFragment(new ActionFragment());
        AppState.getInstance().setSubject(event.getSubject());
    }

    @Subscribe
    public  void onActionSelected(ActionSelectedEvent event){
        Log.d(LOG_TAG, "onActionSelected");
        Action action = event.getAction();
        Log.d(LOG_TAG, action.getDisplayString());
        mSectionsPagerAdapter.setCodingFragment(new ActionFragment());
        AppState.getInstance().setAction(action);
        if(action.getModifierFactory() == null){
            // save selection / jump back to subject selection
        }
        else if(action.getModifierFactory() instanceof SubjectModifierFactory){
            mSectionsPagerAdapter.setCodingFragment(new SubjectModifierFragment());
        }


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        EventBusHolder.register(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {

            String title = "";

            Integer resourceTitle = mSectionsPagerAdapter.getPageTitleResource(i);
            if(resourceTitle != null){
                title = getString(resourceTitle);
            }

            actionBar.addTab(
                    actionBar.newTab()
                            .setText(title)
                            .setTabListener(this));
        }
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
    }
}
