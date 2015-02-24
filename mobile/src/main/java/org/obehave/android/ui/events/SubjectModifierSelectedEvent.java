package org.obehave.android.ui.events;

import org.obehave.model.Subject;

import java.util.List;

public class SubjectModifierSelectedEvent extends GuiEvent{
    List<Subject> subjects;

    public SubjectModifierSelectedEvent(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<Subject> getSubjects(){
        return subjects;
    }
}
