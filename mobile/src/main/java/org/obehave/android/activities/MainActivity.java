package org.obehave.android.activities;


import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import org.obehave.android.R;
import org.obehave.android.fragments.CodingFragment;
import org.obehave.android.fragments.SubjectFragment;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {
                case 0: return CodingFragment.newInstance();
                case 1: return SubjectFragment.newInstance();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
/*
    private GridView gvCoding;
    private Button btnCoding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CodingTableFragment())
                    .commit();
        }

        initGUIComponents();
        Subject s = new Subject("ABC");

    }

    private void initGUIComponents() {
        gvCoding = (GridView) findViewById(R.id.gvCoding);
        btnCoding = (Button) findViewById(R.id.btnCoding);
    }

    public void onCodingButtonClicked(View v){
        Log.d("events", "onCodingButton Clicked");
        //replaceCurrentFragmentBySimpleListViewFragment();
        replaceCurrentFragmentByExpandableListViewFragment();
    }

    private void replaceCurrentFragmentByExpandableListViewFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ExpandableListviewFragment expandableViewFragment = new ExpandableListviewFragment();
        expandableViewFragment.setArguments(new Bundle());
        transaction.replace(R.id.container, expandableViewFragment);
        transaction.setBreadCrumbShortTitle(R.string.bc_group_fragment_subject);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void replaceCurrentFragmentBySimpleListViewFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SimpleListViewFragment simpleListViewFragment = new SimpleListViewFragment();
        simpleListViewFragment.setArguments(new Bundle());
        transaction.replace(R.id.container, simpleListViewFragment);
        transaction.setBreadCrumbShortTitle(R.string.bc_group_fragment_subject);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class CodingTableFragment extends Fragment {

        public CodingTableFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
*/



}
