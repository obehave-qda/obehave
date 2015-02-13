package org.obehave.model;

import org.obehave.events.ChangeEvent;
import org.obehave.events.ChangeType;
import org.obehave.events.EventBusHolder;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.model.tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A study contains multiple subjects, actions and observations.
 */
public class Study extends BaseEntity {
    private static final Logger log = LoggerFactory.getLogger(Study.class);

    private String name;

    private Node<Subject> subjects = new Node<>();
    private Node<Action> actions = new Node<>();
    private Node<Observation> observations = new Node<>();
    private Node<ModifierFactory> modifierFactories = new Node<>();

    public Study(){

    }

    public Study(String name) {
        this.name = name;
    }

    public Node<Subject> getSubjects() {
        return subjects;
    }

    public boolean addSubject(Subject subject) {
        if (subjects.contains(subject)) {
            log.debug("Won't setData another {}", subject);
            return false;
        }

        log.debug("Adding subject {}", subject);

        final boolean added = subjects.addChild(subject);
        EventBusHolder.post(new ChangeEvent<>(subject, ChangeType.CREATE));
        return added;
    }

    public boolean removeSubject(Subject subject) {
        log.debug("Removing subject {}", subject);
        final boolean deleted = subjects.remove(subject);
        EventBusHolder.post(new ChangeEvent<>(subject, ChangeType.DELETE));
        return deleted;
    }

    public Node<Action> getActions() {
        return actions;
    }

    public boolean addAction(Action action) {
        if (actions.contains(action)) {
            log.debug("Won't setData another {}", action);
            return false;
        }

        log.debug("Adding action {}", action);
        final boolean added = actions.addChild(action);
        EventBusHolder.post(new ChangeEvent<>(action, ChangeType.CREATE));
        return added;
    }

    public boolean removeAction(Action action) {
        log.debug("Removing action {}", action);
        final boolean deleted = actions.remove(action);
        EventBusHolder.post(new ChangeEvent<>(action, ChangeType.DELETE));
        return deleted;
    }

    public Node<Observation> getObservations() {
        return observations;
    }

    public boolean addObservation(Observation observation) {
        if (observations.contains(observation)) {
            log.debug("Won't setData another {}", observation);
            return false;
        }

        log.debug("Adding observation {}", observation);
        final boolean added = observations.addChild(observation);
        EventBusHolder.post(new ChangeEvent<>(observation, ChangeType.CREATE));
        return added;
    }

    public boolean addModifierFactory(ModifierFactory<?> modifierFactory) {
        if (modifierFactories.contains(modifierFactory)) {
            log.debug("Won't setData another {}", modifierFactory);
            return false;
        }

        log.debug("Adding modifierFactory {}", modifierFactory);
        final boolean added = modifierFactories.addChild(modifierFactory);
        EventBusHolder.post(new ChangeEvent<>(modifierFactory, ChangeType.CREATE));
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
