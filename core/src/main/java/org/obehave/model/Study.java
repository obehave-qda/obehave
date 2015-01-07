package org.obehave.model;

import org.obehave.events.ChangeEvent;
import org.obehave.events.ChangeType;
import org.obehave.events.EventBusHolder;
import org.obehave.model.domain.Action;
import org.obehave.model.domain.Observation;
import org.obehave.model.domain.Subject;
import org.obehave.model.domain.modifier.ModifierFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A study contains multiple subjects, actions and observations.
 */
public class Study extends BaseEntity {
    private static final Logger log = LoggerFactory.getLogger(Study.class);


    private String name;

    private List<Subject> subjects = new ArrayList<>();
    private List<Action> actions = new ArrayList<>();
    private List<Observation> observations = new ArrayList<>();
    private List<ModifierFactory> modifierFactories = new ArrayList<>();

    public Study(){

    }

    public Study(String name) {
        this.name = name;
    }

    public List<Subject> getSubjects() {
        return Collections.unmodifiableList(subjects);
    }

    public boolean addSubject(Subject subject) {
        if (subjects.contains(subject)) {
            log.debug("Won't add another {}", subject);
            return false;
        }

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
        if (actions.contains(action)) {
            log.debug("Won't add another {}", action);
            return false;
        }

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
        if (observations.contains(observation)) {
            log.debug("Won't add another {}", observation);
            return false;
        }

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

    // ONLY FOR TEMPORARILY TESTING!
    public void addRandomSubject(String key) {
        addSubject(new Subject(getRandomString("Wolf " + key)));
    }

    public void addRandomAction(String key) {
        addAction(new Action(getRandomString("Action " + key)));
    }

    public void addRandomObservation(String key) {
        addObservation(new Observation(getRandomString("Observation " + key)));
    }

    private static String getRandomString(String prefix) {
        int number = (int) (Math.random() * 5);
        return prefix + " " + number;
    }
}
