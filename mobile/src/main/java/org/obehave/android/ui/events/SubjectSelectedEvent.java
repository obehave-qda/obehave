package org.obehave.android.ui.events;

import org.obehave.model.Subject;

public class SubjectSelectedEvent extends GuiEvent {
    private final Subject subject;

    public SubjectSelectedEvent(Subject subject) {
        this.subject = subject;
    }

    public Subject getSubject(){
        return subject;
    }



}
