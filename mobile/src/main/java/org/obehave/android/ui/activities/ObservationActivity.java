package org.obehave.android.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import org.obehave.android.R;
import org.obehave.android.ui.fragments.ObservationFragment;
import org.obehave.service.Study;

public class ObservationActivity extends FragmentActivity {
    public static final String RESULT_OBSERVATION = "android.ui.observation";
    public final static String ARG_STUDY = "org.obehave.observation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            Study study = (Study) bundle.getSerializable(ARG_STUDY);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ObservationFragment.newInstance(study))
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_observation, menu);
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


}
