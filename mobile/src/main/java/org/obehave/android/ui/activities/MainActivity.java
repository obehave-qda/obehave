package org.obehave.android.ui.activities;


import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.util.Log;
import com.google.common.eventbus.Subscribe;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import org.obehave.android.R;
import org.obehave.android.database.*;
import org.obehave.android.ui.adapters.MyPagerAdapter;
import org.obehave.android.ui.events.CodingButtonClickedEvent;
import org.obehave.android.ui.events.ListViewItemSelectedEvent;
import org.obehave.android.ui.utils.FragmentFactory;
import org.obehave.android.ui.utils.MyViewPager;
import org.obehave.events.EventBusHolder;

import java.sql.SQLException;


public class MainActivity extends FragmentActivity {

    private MyPagerAdapter  pageAdapter;
    private MyViewPager pager;
    private final String LOG_TAG = getClass().getSimpleName();
    private MyDatabaseHelper databaseHelper;

    private SubjectDbAdapter subjectDbAdapter;
    private SubjectGroupDbAdapter subjectGroupDbAdapter;
    private ActionDbAdapter actionDbAdapter;
    private ActionGroupDbAdapter actionGroupDbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = (MyViewPager) findViewById(R.id.viewPager);
        pageAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pageAdapter.add(FragmentFactory.getFragment("Coding", new Bundle()));
        pageAdapter.add(FragmentFactory.getFragment("Subjects", new Bundle()));
        pageAdapter.add(FragmentFactory.getFragment("Actions", new Bundle()));
        pageAdapter.add(FragmentFactory.getFragment("Modifier", new Bundle()));

        pageAdapter.setFakedCount(2);
        pager.setAdapter(pageAdapter);

        EventBusHolder.register(this);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position < pageAdapter.getCount()-1 && position > 0){
                    pageAdapter.setFakedCount(position+1);
                    pageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }


    @Subscribe
    public void onCodingButtonClicked(CodingButtonClickedEvent event){
        Log.d(LOG_TAG, "MainActivity codingButton Clicked");
        pager.setCurrentItem(1);
    }

    @Subscribe
    public void onListViewItemSelected(ListViewItemSelectedEvent<String> event){
        Log.d(LOG_TAG, "onListViewItemSelected");
        if(event.getType().equalsIgnoreCase("subject")) {
            Log.d(LOG_TAG, "Subject Selected");
            pageAdapter.setFakedCount(3);
            pageAdapter.notifyDataSetChanged();
            pager.setCurrentItem(2);
        }

        if(event.getType().equalsIgnoreCase("actions")) {
            pageAdapter.setFakedCount(4);
            pageAdapter.notifyDataSetChanged();
            pager.setCurrentItem(3);
            Log.d(LOG_TAG, "action Selected");
        }

        if(event.getType().equalsIgnoreCase("modifier")) {
            pageAdapter.setFakedCount(2);
            pageAdapter.notifyDataSetChanged();
            pager.setCurrentItem(0);
            Log.d(LOG_TAG, "Modifier Selected");
        }

    }


    @Override
    public void onBackPressed() {

        if (pager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    public MyDatabaseHelper getHelper() {
        if (databaseHelper == null) {
            Log.d(LOG_TAG, "getHelper() get database helper back");
            databaseHelper = OpenHelperManager.getHelper(this, MyDatabaseHelper.class);
            databaseHelper.getWritableDatabase();
        }
        return databaseHelper;
    }

    public AbstractDbAdapter getSubjectAdapter(){
        if(subjectDbAdapter == null){
            try {
                subjectDbAdapter = new SubjectDbAdapter(getApplicationContext(), getHelper());
            } catch (SQLException e) {
                Log.e(LOG_TAG, "Error - getSubjectAdapter - " + e.getMessage());
                e.printStackTrace();
            }
        }

        return subjectDbAdapter;
    }

    public AbstractDbAdapter getSubjectGroupAdapter(){
        if(subjectGroupDbAdapter == null){
            try {
                subjectGroupDbAdapter = new SubjectGroupDbAdapter(getApplicationContext(), getHelper());
            } catch (SQLException e) {
                Log.e(LOG_TAG, "Error - getSubjectGroupAdapter - " + e.getMessage());
                e.printStackTrace();
            }
        }

        return subjectGroupDbAdapter;
    }

    public AbstractDbAdapter getActionGroupAdapter(){
        if(actionGroupDbAdapter == null){
            try {
                actionGroupDbAdapter = new ActionGroupDbAdapter(getApplicationContext(), getHelper());
            } catch (SQLException e) {
                Log.e(LOG_TAG, "Error - getActionGroupAdapter - " + e.getMessage());
                e.printStackTrace();
            }
        }

        return actionGroupDbAdapter;
    }

    public AbstractDbAdapter getActionAdapter(){
        if(actionDbAdapter == null){
            try {
                actionDbAdapter = new ActionDbAdapter(getApplicationContext(), getHelper());
            } catch (SQLException e) {
                Log.e(LOG_TAG, "Error - getActionAdatper - " + e.getMessage());
                e.printStackTrace();
            }
        }

        return actionDbAdapter;
    }



}
