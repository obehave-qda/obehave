package org.obehave.android.application;

import org.joda.time.DateTime;
import org.obehave.android.ui.fragments.behaviors.SortType;
import org.obehave.model.Action;
import org.obehave.model.Subject;
import org.obehave.model.modifier.Modifier;

public class ApplicationState {
    private static ApplicationState ourInstance = new ApplicationState();
    private Subject subject;
    private Action action;
    private Modifier modifier;

    private DateTime codingStarted;

    private int selectedSubjectSortType = SortType.DEFAULT;
    private int selectedActionSortType  = SortType.DEFAULT;

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

    public void setCodingStartedTime(){
        this.codingStarted = DateTime.now();
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

    public int getSelectedSubjectSortType() {
        return selectedSubjectSortType;
    }

    public void setSelectedSubjectSortType(int selectedSubjectSortType) {
        this.selectedSubjectSortType = selectedSubjectSortType;
    }

    public int getSelectedActionSortType() {
        return selectedActionSortType;
    }

    public void setSelectedActionSortType(int selectedActionSortType) {
        this.selectedActionSortType = selectedActionSortType;
    }
}
