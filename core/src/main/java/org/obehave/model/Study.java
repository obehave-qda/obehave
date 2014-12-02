package org.obehave.model;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * A study contains multiple subjects, actions and observations.
 */
public class Study extends BaseEntity {
    private Set<Subject> subjects = new TreeSet<>();
    private Set<Action> actions = new TreeSet<>();
    private Set<Observation> observations = new TreeSet<>();

    private String name;

    public Study(String name) {
        this.name = name;
    }

    public Set<Subject> getSubjects() {
        return Collections.unmodifiableSet(subjects);
    }

    public boolean addSubject(Subject subject) {
        return subjects.add(subject);
    }

    public Set<Action> getActions() {
        return Collections.unmodifiableSet(actions);
    }

    public boolean addAction(Action action) {
        return actions.add(action);
    }

    public Set<Observation> getObservations() {
        return Collections.unmodifiableSet(observations);
    }

    public boolean addObservation(Observation observation) {
        return observations.add(observation);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
