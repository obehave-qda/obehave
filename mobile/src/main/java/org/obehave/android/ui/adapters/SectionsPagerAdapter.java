package org.obehave.android.ui.adapters;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import org.obehave.android.R;
import org.obehave.android.application.MyApplication;
import org.obehave.android.ui.activities.MainActivity;
import org.obehave.android.ui.fragments.CodingListFragment;
import org.obehave.android.ui.fragments.DecimalRangeModifierFragment;
import org.obehave.android.ui.fragments.EnumerationModifierFragment;
import org.obehave.android.ui.fragments.SubjectModifierFragment;
import org.obehave.model.Action;
import org.obehave.model.modifier.ModifierFactory;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    final static int NUM_FRAGMENTS = 3;
    final static int POSITION_ALL_CODING_FRAGMENT = 0;
    final static int POSITION_RUNNING_CODING_FRAGMENT = 1;
    final static int POSITION_CODING_FRAGMENT = 2;
    final String LOG_TAG = getClass().getSimpleName();
    private final MainActivity activity;

    private FragmentManager fragmentManager;
    private ViewPager container;
    private ActionBar actionBar;


    public void changeCodingFragment(Fragment fragment){

        if(fragment != null) {
            Log.d(LOG_TAG, "changeCodingFragment:");
            Log.d(LOG_TAG, "Count. switch to next: " + getApplication().getBackStackHistory().size());
            addFragmentToHistory(fragment);
            notifyDataSetChanged();
        }
    }

    private MyApplication getApplication(){
        return (MyApplication)activity.getApplication();
    }

    public void changeCodingFragment(Action action){

        Fragment fragment = null;

        if (action.getModifierFactory().getType() == ModifierFactory.Type.SUBJECT_MODIFIER_FACTORY) {
            fragment = SubjectModifierFragment.newInstance(POSITION_CODING_FRAGMENT);
        } else if (action.getModifierFactory().getType() == ModifierFactory.Type.DECIMAL_RANGE_MODIFIER_FACTORY) {
            fragment = DecimalRangeModifierFragment.newInstance(POSITION_CODING_FRAGMENT);
        } else if (action.getModifierFactory().getType() == ModifierFactory.Type.ENUMERATION_MODIFIER_FACTORY) {
            fragment = EnumerationModifierFragment.newInstance(POSITION_CODING_FRAGMENT);
        }

        changeCodingFragment(fragment);
    }

    public SectionsPagerAdapter(MainActivity activity, ActionBar actionBar, ViewPager container, FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
        this.container = container;
        this.actionBar = actionBar;
        this.activity = activity;
    }


    private void addFragmentToHistory(Fragment fragment){
        Log.d(LOG_TAG, "addFragmentToHistory");
        getApplication().getBackStackHistory().push(fragmentManager, fragment);

    }

    @Override
    public Fragment getItem(int position) {

        switch(position){
            case POSITION_ALL_CODING_FRAGMENT:
                return CodingListFragment.newInstance(position + 1, CodingListFragment.CODING_TYPE_ALL);
            case POSITION_RUNNING_CODING_FRAGMENT:
                return CodingListFragment.newInstance(position + 1, CodingListFragment.CODING_TYPE_RUNNING);
            case POSITION_CODING_FRAGMENT:
            default:
                return getApplication().getBackStackHistory().peek();

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
            if (getApplication().getBackStackHistory().size() > 1) {
                getApplication().getBackStackHistory().pop(fragmentManager);
                notifyDataSetChanged();
                return true;
            }
            notifyDataSetChanged();
        }
        return false;
    }


    public void clearHistory(){
        getApplication().getBackStackHistory().clear(fragmentManager);
    }


}
