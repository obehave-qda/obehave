package org.obehave.android.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import org.obehave.android.R;
import org.obehave.android.ui.fragments.AllCodingsFragment;
import org.obehave.android.ui.fragments.RunningCodingsFragment;
import org.obehave.android.ui.fragments.SubjectFragment;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {


    final String LOG_TAG = getClass().getSimpleName();
    final static int POSITION_ALL_CODING_FRAGMENT = 0;
    final static int POSITION_RUNNING_CODING_FRAGMENT = 1;
    final static int POSITION_CODING_FRAGMENT = 2;

    private Fragment currentCodingFragment;

    private FragmentManager fragmentManager;


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        switch(position){
            case POSITION_ALL_CODING_FRAGMENT:
                return AllCodingsFragment.newInstance(position + 1);
            case POSITION_RUNNING_CODING_FRAGMENT:
                return RunningCodingsFragment.newInstance(position + 1);
            case POSITION_CODING_FRAGMENT:
                if(currentCodingFragment != null){
                    return currentCodingFragment;
                }
                Log.d(LOG_TAG, "POSITION_CODING_FRAGMENT");
            default:
                return SubjectFragment.newInstance(position + 1);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    public void setCodingFragment(Fragment fragment){
        currentCodingFragment = fragment;
        this.notifyDataSetChanged();
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
}
