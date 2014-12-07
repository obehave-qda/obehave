package org.obehave.model;

import com.google.common.eventbus.EventBus;
import org.obehave.events.ChangeEvent;
import org.obehave.events.ChangeType;
import org.obehave.events.EventBusHolder;
import org.obehave.model.events.ActionChangeEvent;
import org.obehave.model.events.ObservationChangeEvent;
import org.obehave.model.events.SubjectChangeEvent;

import java.util.*;

/**
 * A study contains multiple subjects, actions and observations.
 */
public class Study extends BaseEntity {
    private final EventBus eventBus = EventBusHolder.getEventBus();

    private List<Subject> subjects = new ArrayList<>();
    private List<Action> actions = new ArrayList<>();
    private List<Observation> observations = new ArrayList<>();

    private String name;

    public Study(String name) {
        this.name = name;
    }

    public List<Subject> getSubjects() {
        return Collections.unmodifiableList(subjects);
    }

    public boolean addSubject(Subject subject) {
        final boolean result = subjects.add(subject);
        eventBus.post(new SubjectChangeEvent(subject, ChangeType.CREATE));
        return result;
    }

    public List<Action> getActions() {
        return Collections.unmodifiableList(actions);
    }

    public boolean addAction(Action action) {
        final boolean result = actions.add(action);
        eventBus.post(new ActionChangeEvent(action, ChangeType.CREATE));
        return result;
    }

    public List<Observation> getObservations() {
        return Collections.unmodifiableList(observations);
    }

    public boolean addObservation(Observation observation) {
        final boolean result = observations.add(observation);
        eventBus.post(new ObservationChangeEvent(observation, ChangeType.CREATE));
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
