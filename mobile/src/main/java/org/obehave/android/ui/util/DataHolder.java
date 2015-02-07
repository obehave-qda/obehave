package org.obehave.android.ui.util;

import org.obehave.android.ui.exceptions.UiException;
import org.obehave.model.Action;
import org.obehave.model.Subject;

import java.util.Collections;
import java.util.List;

public class DataHolder {
    private static DataHolder instance = new DataHolder();
    private List<Subject> subjects;
    private List<Action> actions;

    public static DataHolder getInstance() {
        return instance;
    }

    private DataHolder() {
    }

    public List<Subject> getAllSubjects() throws UiException {
        if(subjects != null) {
            return subjects;
        }

        return Collections.emptyList();
    }

    public List<Action> getAllActions() throws UiException {
        if(actions != null) {
        }

        return Collections.emptyList();
    }
}
