package org.obehave.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
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
    @ForeignCollectionField
    private Collection<Subject> subjects = new ArrayList<>();
    @ForeignCollectionField
    private Collection<Action> actions = new ArrayList<>();
    @ForeignCollectionField
    private Collection<Observation> observations = new ArrayList<>();

    @DatabaseField
    private String name;

    public Study(){

    }

    public Study(String name) {
        this.name = name;
    }

    public Collection<Subject> getSubjects() {
        return Collections.unmodifiableCollection(subjects);
    }

    public boolean addSubject(Subject subject) {
        if (subjects.contains(subject)) {
            log.debug("Won't add another {}", subject);
            return false;
        }

        log.debug("Adding subject {}", subject);

        final boolean added = subjects.add(subject);
        EventBusHolder.post(new ChangeEvent<Subject>(subject, ChangeType.CREATE));
        return added;
    }

    public boolean removeSubject(Subject subject) {
        log.debug("Removing subject {}", subject);
        final boolean deleted = subjects.remove(subject);
        EventBusHolder.post(new ChangeEvent<Subject>(subject, ChangeType.DELETE));
        return deleted;
    }

    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actions);
    }

    public boolean addAction(Action action) {
        if (actions.contains(action)) {
            log.debug("Won't add another {}", action);
            return false;
        }

        log.debug("Adding action {}", action);
        final boolean added = actions.add(action);
        EventBusHolder.post(new ChangeEvent<Action>(action, ChangeType.CREATE));
        return added;
    }

    public boolean removeAction(Action action) {
        log.debug("Removing action {}", action);
        final boolean deleted = actions.remove(action);
        EventBusHolder.post(new ChangeEvent<Action>(action, ChangeType.DELETE));
        return deleted;
    }

    public Collection<Observation> getObservations() {
        return Collections.unmodifiableCollection(observations);
    }

    public boolean addObservation(Observation observation) {
        if (observations.contains(observation)) {
            log.debug("Won't add another {}", observation);
            return false;
        }

        log.debug("Adding observation {}", observation);
        final boolean added = observations.add(observation);
        EventBusHolder.post(new ChangeEvent<Observation>(observation, ChangeType.CREATE));
        return added;
    }

    public boolean removeObservation(Observation observation) {
        log.debug("Removing observation {}", observation);
        final boolean deleted = observations.remove(observation);
        EventBusHolder.post(new ChangeEvent<Observation>(observation, ChangeType.DELETE));
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

    private String getRandomString(String prefix) {
        int number = (int) (Math.random() * 5);
        return prefix + " " + number;
    }
}
