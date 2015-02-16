package org.obehave.android.services;

import org.obehave.model.Action;
import org.obehave.model.Subject;
import org.obehave.model.coding.Coding;
import org.obehave.model.modifier.Modifier;

import java.util.List;

public class ApplicationState {
    private static ApplicationState ourInstance = new ApplicationState();

    private Subject subject;
    private Action action;
    private Modifier modifier;
    private List<Coding> runningCodings;
    private List<Coding> allCodings;

    public static ApplicationState getInstance() {
        return ourInstance;
    }

    private ApplicationState() {
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

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }
}
