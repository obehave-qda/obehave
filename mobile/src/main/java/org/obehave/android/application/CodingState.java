package org.obehave.android.application;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.obehave.model.Action;
import org.obehave.model.Subject;
import org.obehave.model.modifier.Modifier;

import java.io.Serializable;

public class CodingState implements Serializable{
    private static final long serialVersionUID = 1L;


    private DateTime startTime;
    private Subject subject;
    private Action action;
    private Modifier modifier;


    public CodingState setStartTime(LocalDateTime now){
        this.startTime = DateTime.now();
        return this;
    }

    public CodingState setSubject(Subject subject) {
        this.subject = subject;
        return this;
    }

    public CodingState setAction(Action action) {
        this.action = action;
        return this;
    }

    public CodingState setModifier(Modifier modifier){
        this.modifier = modifier;
        return this;
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
