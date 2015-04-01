package org.obehave.android.ui.fragments.behaviors;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Adds additonal functionality to
 * an Fragment
 */
public interface FragmentBehavior {

    /**
     * Init Behavior
     *
     * @param activity
     * @param fragment
     * @param rootView
     */
    public void init(Activity activity, Fragment fragment, View rootView, Bundle settings);


}
