package org.obehave.android.ui.fragments;

import android.support.v4.app.ListFragment;
import org.joda.time.DateTime;

public abstract class CodingListBaseFragment extends ListFragment{
    protected final String LOG_TAG = this.getClass().getSimpleName();
    public abstract void updateTimer(DateTime startDateTime);
}
