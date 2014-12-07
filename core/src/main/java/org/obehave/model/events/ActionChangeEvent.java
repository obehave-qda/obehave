package org.obehave.model.events;

import org.obehave.events.ChangeEvent;
import org.obehave.events.ChangeType;
import org.obehave.model.Action;
import org.obehave.model.Subject;

/**
 * Created by Markus on 07.12.2014.
 */
public class ActionChangeEvent extends ChangeEvent<Action> {
    public ActionChangeEvent(Action changedAction, ChangeType changeType) {
        super(changedAction, changeType);
    }
}
