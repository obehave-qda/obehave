package org.obehave.android.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import com.google.common.eventbus.Subscribe;
import org.obehave.android.R;
import org.obehave.android.ui.events.FileChoosenEvent;
import org.obehave.android.ui.events.OpenFileChooserEvent;
import org.obehave.android.ui.fragments.DirectoryListFragment;
import org.obehave.android.ui.fragments.WelcomeFragment;
import org.obehave.events.EventBusHolder;

import java.io.File;


public class OpenStudyActivity extends FragmentActivity {

    private final String LOG_TAG = this.getClass().getSimpleName();
    public final static String RESULT_OBJECT_FILENAME = "org.obehave.OpenStudyActivity.filename";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.d(LOG_TAG, "register");
        EventBusHolder.register(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new WelcomeFragment())
                    .commit();
        }
    }
    @Subscribe
    public void loadFileChooserFragment(OpenFileChooserEvent event){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new DirectoryListFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Subscribe
    public void fileChoosen(FileChoosenEvent event) {
        File file = event.getFile();
        Intent resultIntent = new Intent();
        resultIntent.putExtra(RESULT_OBJECT_FILENAME, file.getAbsolutePath());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "destroy");
    }
}
