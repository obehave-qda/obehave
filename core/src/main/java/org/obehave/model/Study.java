package org.obehave.model;

import java.util.*;

/**
 * A study contains multiple subjects, actions and observations.
 */
public class Study extends BaseEntity {
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
        setChanged();
        notifyObservers(subject);
        return result;
    }

    public List<Action> getActions() {
        return Collections.unmodifiableList(actions);
    }

    public boolean addAction(Action action) {
        final boolean result = actions.add(action);
        setChanged();
        notifyObservers(action);
        return result;
    }

    public List<Observation> getObservations() {
        return Collections.unmodifiableList(observations);
    }

    public boolean addObservation(Observation observation) {
        final boolean result = observations.add(observation);
        setChanged();
        notifyObservers(observation);
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
