package org.obehave.events;

import org.obehave.model.Action;
import org.obehave.model.Observation;
import org.obehave.model.Subject;
import org.obehave.model.modifier.ModifierFactory;

/**
 * Class to summarize change events for different entities
 */
public class Changes {
    public static class SubjectChange extends ChangeEvent<Subject> {
        public SubjectChange(Subject changedSubject, ChangeType changeType) {
            super(changedSubject, changeType);
        }
    }

    public static class ActionChange extends ChangeEvent<Action> {
        public ActionChange(Action changedAction, ChangeType changeType) {
            super(changedAction, changeType);
        }
    }

    public static class ModifierFactoryChange extends ChangeEvent<ModifierFactory> {
        public ModifierFactoryChange(ModifierFactory changedModifierFactory, ChangeType changeType) {
            super(changedModifierFactory, changeType);
        }
    }

    public static class ObservationChange extends ChangeEvent<Observation> {
        public ObservationChange(Observation changedObservation, ChangeType changeType) {
            super(changedObservation, changeType);
        }
    }
}
