package org.obehave.android.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments;
        private Integer fakeCount;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<Fragment>();

        }

        public void add(Fragment fragment){
            fragments.add(fragment);
        }

        public void remove(int pos){
            fragments.remove(pos);
        }

        public void setFakedCount(int fakedCount){
            this.fakeCount = fakedCount;
        }

        @Override
        public Fragment getItem(int pos) {
            return fragments.get(pos);
        }

        @Override
        public int getCount() {
            if(fakeCount == null) {
                return fragments.size();
            }

            return fakeCount;
        }
    }