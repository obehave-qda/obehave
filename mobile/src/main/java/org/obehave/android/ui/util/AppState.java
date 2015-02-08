package org.obehave.android.ui.util;

import org.obehave.model.Action;
import org.obehave.model.Subject;

public class AppState {
    private static AppState ourInstance = new AppState();

    private Subject subject;
    private Action action;

    public static AppState getInstance() {
        return ourInstance;
    }

    private AppState() {
    }

    public void setSubject(Subject subject){
        this.subject = subject;
    }

    public void setAction(Action action){
        this.action = action;
    }

    public Subject getSubject() {
        return subject;
    }

    public Action getAction() {
        return action;
    }

}
