package org.obehave.android.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import org.obehave.android.R;
import org.obehave.android.fragments.GroupFragment;
import org.obehave.model.Subject;


public class MainActivity extends Activity {

    private GridView gvCoding;
    private Button btnCoding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new CodingTableFragment())
                    .commit();
        }

        initGUIComponents();
        Subject s = new Subject();
        s.setName("ABC");

    }

    private void initGUIComponents() {
        gvCoding = (GridView) findViewById(R.id.gvCoding);
        btnCoding = (Button) findViewById(R.id.btnCoding);
    }

    public void onCodingButtonClicked(View v){
        Log.d("events", "onCodingButton Clicked");
        replaceCurrentFragmentByGroupFragment();
    }

    private void replaceCurrentFragmentByGroupFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        GroupFragment groupFragment = new GroupFragment();
        groupFragment.setArguments(new Bundle());
        transaction.replace(R.id.container, groupFragment);
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




}
