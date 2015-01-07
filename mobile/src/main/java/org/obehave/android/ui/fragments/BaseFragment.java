package org.obehave.android.ui.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import org.obehave.android.database.MyDatabaseHelper;

public class BaseFragment extends Fragment {

    private MyDatabaseHelper databaseHelper = null;
    protected final String LOG_TAG = getClass().getSimpleName();

    protected MyDatabaseHelper getHelper() {
        if (databaseHelper == null) {
            Log.d(LOG_TAG, "getHelper() get database helper back");
            databaseHelper = OpenHelperManager.getHelper(getActivity(), MyDatabaseHelper.class);
            databaseHelper.getWritableDatabase();
        }
        return databaseHelper;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {

            databaseHelper.close();
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
            Log.d(LOG_TAG, "disconnect database and helper");
        }
    }

}
