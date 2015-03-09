package org.obehave.android.ui.adapters;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import org.obehave.android.R;
import org.obehave.android.ui.fragments.AllCodingsFragment;
import org.obehave.android.ui.fragments.RunningCodingsFragment;

import java.util.Stack;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    final static int NUM_FRAGMENTS = 3;
    final static int POSITION_ALL_CODING_FRAGMENT = 0;
    final static int POSITION_RUNNING_CODING_FRAGMENT = 1;
    final static int POSITION_CODING_FRAGMENT = 2;
    final String LOG_TAG = getClass().getSimpleName();

    private FragmentManager fragmentManager;
    private ViewPager container;
    private Stack<Fragment> history;
    private ActionBar actionBar;


    public void switchToNextCodingFragment(Fragment fragment){

        if(fragment != null) {
            Log.d(LOG_TAG, "switchToNextCodingFragment:");
            Log.d(LOG_TAG, "Count. switch to next: " + history.size());
            addFragmentToHistory(fragment);
            notifyDataSetChanged();
        }
    }

    public SectionsPagerAdapter(ActionBar actionBar, ViewPager container, FragmentManager fm, Fragment currentCodingFragment) {
        super(fm);
        fragmentManager = fm;
        this.container = container;
        this.actionBar = actionBar;
        history = new Stack<Fragment>();
        addFragmentToHistory(currentCodingFragment);
    }

    private void addFragmentToHistory(Fragment fragment){
        Log.d(LOG_TAG, "addFragmentToHistory");
        history.push(fragment);
        fragmentManager.beginTransaction().addToBackStack(null).commit();

    }

    @Override
    public Fragment getItem(int position) {

        switch(position){
            case POSITION_ALL_CODING_FRAGMENT:
                return AllCodingsFragment.newInstance(position + 1);
            case POSITION_RUNNING_CODING_FRAGMENT:
                return RunningCodingsFragment.newInstance(position + 1);
            case POSITION_CODING_FRAGMENT:
            default:
                Log.d(LOG_TAG, "onReturningCodingFragment");
                return history.peek();

        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return NUM_FRAGMENTS;
    }

    public Integer getPageTitleResource(int position) {
        switch (position) {
            case 0:
                return R.string.title_section1;
            case 1:
                return R.string.title_section2;
            case 2:
                return R.string.title_section3;
        }
        return null;
    }

    public boolean back(){
        if(actionBar.getSelectedTab().getPosition() == POSITION_CODING_FRAGMENT) {
            if (history.size() > 1) {
                Log.d(LOG_TAG, "Count. switch to next: " + history.size());
                popFragmentFromHistory();
                notifyDataSetChanged();
                return true;
            }

            notifyDataSetChanged();
        }
        Log.d(LOG_TAG, "finish activity -> when last element");
        return false;
    }

    private void popFragmentFromHistory() {
        Log.d(LOG_TAG, "popFragmentFromHistory");
        history.pop();
    }

    public void clearHistory(){
        history.clear();
    }
}
