package org.obehave.android.application;

import org.joda.time.DateTime;
import org.obehave.model.Action;
import org.obehave.model.Subject;
import org.obehave.model.modifier.Modifier;

/**
 * Created by patrick on 04.04.2015.
 */
public class CodingState {

    private DateTime startTime;
    private Subject subject;
    private Action action;
    private Modifier modifier;

    public void setStartTime(DateTime startTime){
        this.startTime = startTime;
    }

    public void setStartTime(){
        this.startTime = DateTime.now();
    }

    public void setSubject(Subject subject) {
        this.startTime = DateTime.now();
        this.subject = subject;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setModifier(Modifier modifier){
        this.modifier = modifier;
    }


    public DateTime getStartTime() {
        return startTime;
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
}
