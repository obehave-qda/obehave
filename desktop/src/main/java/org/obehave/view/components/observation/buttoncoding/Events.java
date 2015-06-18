package org.obehave.view.components.observation.buttoncoding;

import org.obehave.events.AbstractEvent;
import org.obehave.model.Action;
import org.obehave.model.Subject;

/**
 * @author Markus Möslinger
 */
class Events {
    public static class SubjectClicked extends AbstractEvent<Subject> {
        public SubjectClicked(Subject subject) {
            super(subject);
        }
    }

    public static class ActionClicked extends AbstractEvent<Action> {
        public ActionClicked(Action action) {
            super(action);
        }
    }

    public static class ModifierClicked extends AbstractEvent<String> {
        public ModifierClicked(String modifier) {
            super(modifier);
        }
    }
}
