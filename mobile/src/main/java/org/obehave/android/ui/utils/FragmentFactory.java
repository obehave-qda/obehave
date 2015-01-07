package org.obehave.android.ui.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import org.obehave.android.ui.fragments.*;

public class FragmentFactory {

    public static Fragment getFragment(String name, Bundle bundle){
        if(name.equals("Actions")){
            return createActionsFragment(bundle);
        }
        else if(name.equalsIgnoreCase("Subjects")){
            return createSubjectFragment(bundle);
        }
        else if(name.equals("Coding")){
            return createCodingFragment(bundle);
        }
        else if(name.equals("Modifier")){
            return createModfierFragment(bundle);
        }
        else if(name.equals("SimpleListView")){
            return createSimpleListViewFragment(bundle);
        }
        else {
            throw new IllegalArgumentException("Class " + name + "Fragment not found!");
        }
    }

    private static Fragment createSimpleListViewFragment(Bundle bundle) {
        SimpleListViewFragment f = new SimpleListViewFragment();
        f.setArguments(bundle);

        return f;
    }

    private static Fragment createCodingFragment(Bundle bundle) {
        CodingFragment f = new CodingFragment();
        f.setArguments(bundle);

        return f;
    }

    private static Fragment createSubjectFragment(Bundle bundle) {
        SubjectFragment f = new SubjectFragment();
        f.setArguments(bundle);

        return f;
    }

    private static Fragment createActionsFragment(Bundle bundle) {
        ActionsFragment f = new ActionsFragment();
        f.setArguments(bundle);

        return f;
    }

    private static Fragment createModfierFragment(Bundle bundle) {
        ModifierFragment f = new ModifierFragment();
        f.setArguments(bundle);

        return f;
    }


}
