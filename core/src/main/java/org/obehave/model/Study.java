package org.obehave.model;

import com.google.common.eventbus.EventBus;
import org.obehave.events.ChangeEvent;
import org.obehave.events.ChangeType;
import org.obehave.events.EventBusHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * A study contains multiple subjects, actions and observations.
 */
public class Study extends BaseEntity {
    private static final Logger log = LoggerFactory.getLogger(Study.class);

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
        log.debug("Adding subject {}", subject);
        final boolean added = subjects.add(subject);
        EventBusHolder.post(new ChangeEvent<>(subject, ChangeType.CREATE));
        return added;
    }

    public boolean removeSubject(Subject subject) {
        log.debug("Removing subject {}", subject);
        final boolean deleted = subjects.remove(subject);
        EventBusHolder.post(new ChangeEvent<>(subject, ChangeType.DELETE));
        return deleted;
    }

    public List<Action> getActions() {
        return Collections.unmodifiableList(actions);
    }

    public boolean addAction(Action action) {
        log.debug("Adding action {}", action);
        final boolean added = actions.add(action);
        EventBusHolder.post(new ChangeEvent<>(action, ChangeType.CREATE));
        return added;
    }

    public boolean removeAction(Action action) {
        log.debug("Removing action {}", action);
        final boolean deleted = actions.remove(action);
        EventBusHolder.post(new ChangeEvent<>(action, ChangeType.DELETE));
        return deleted;
    }

    public List<Observation> getObservations() {
        return Collections.unmodifiableList(observations);
    }

    public boolean addObservation(Observation observation) {
        log.debug("Adding observation {}", observation);
        final boolean added = observations.add(observation);
        EventBusHolder.post(new ChangeEvent<>(observation, ChangeType.CREATE));
        return added;
    }

    public boolean removeObservation(Observation observation) {
        log.debug("Removing observation {}", observation);
        final boolean deleted = observations.remove(observation);
        EventBusHolder.post(new ChangeEvent<>(observation, ChangeType.DELETE));
        return deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addRandomSubject(String key) {
        addSubject(new Subject(getRandomString("Subject " + key)));
    }

    public void addRandomAction(String key) {
        addAction(new Action(getRandomString("Action " + key)));
    }

    public void addRandomObservation(String key) {
        addObservation(new Observation(getRandomString("Observation " + key)));
    }

    private String getRandomString(String prefix) {
        int number = (int) (Math.random() * 5);
        return prefix + " " + number;
    }
}
