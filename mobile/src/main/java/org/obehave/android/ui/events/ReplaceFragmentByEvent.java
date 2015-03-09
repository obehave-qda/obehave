package org.obehave.android.ui.events;

import android.support.v4.app.Fragment;

public class ReplaceFragmentByEvent {
    private final Fragment fragment;

    public ReplaceFragmentByEvent(Fragment fragment) {
       this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
