package org.obehave.android.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import org.obehave.android.R;
import org.obehave.android.ui.fragments.AllCodingsFragment;
import org.obehave.android.ui.fragments.CodingFragment;
import org.obehave.android.ui.fragments.RunningCodingsFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    final static int POSITION_ALL_CODING_FRAGMENT = 0;
    final static int POSITION_RUNNING_CODING_FRAGMENT = 1;
    final static int POSITION_CODING_FRAGMENT = 2;


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
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
            default:
                return CodingFragment.newInstance(position + 1);
        }


    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
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
