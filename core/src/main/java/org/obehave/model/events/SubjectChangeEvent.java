package org.obehave.model.events;

import org.obehave.events.ChangeEvent;
import org.obehave.events.ChangeType;
import org.obehave.model.Subject;

/**
 * Created by Markus on 07.12.2014.
 */
public class SubjectChangeEvent extends ChangeEvent<Subject> {
    public SubjectChangeEvent(Subject changedSubject, ChangeType changeType) {
        super(changedSubject, changeType);
    }
}
