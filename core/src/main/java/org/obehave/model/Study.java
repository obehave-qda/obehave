package org.obehave.model;

import org.obehave.events.ChangeEvent;
import org.obehave.events.ChangeType;
import org.obehave.events.EventBusHolder;
import org.obehave.model.modifier.ModifierFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * A study contains multiple subjects, actions and observations.
 */
public class Study {
    private static final Logger log = LoggerFactory.getLogger(Study.class);

    private String name;

    private Node subjects = new Node(Subject.class);
    private Node actions = new Node(Action.class);
    private Node observations = new Node(Observation.class);
    private Node modifierFactories = new Node(ModifierFactory.class);

    private File savePath;

    private Study(){

    }

    @Deprecated
    public Study(String name) {
        this.name = name;
    }

    public static Study create(File savePath) {
        log.info("Creating new study at {}", savePath);
        final Study study = new Study();
        study.setSavePath(savePath);
        return study;
    }

    public static Study load(File savePath) {
        log.info("Loading existing study from {}", savePath);
        final Study study = new Study();
        study.setSavePath(savePath);
        // TODO loading magic with daos and stuff
        return study;
    }

    public Node getSubjects() {
        return subjects;
    }

    public boolean addSubject(Subject subject) {
        if (subjects.contains(subject)) {
            log.debug("Won't setData another {}", subject);
            return false;
        }

        log.debug("Adding subject {}", subject);

        subjects.addChild(subject);
        EventBusHolder.post(new ChangeEvent<>(subject, ChangeType.CREATE));
        return true;
    }

    public boolean removeSubject(Subject subject) {
        log.debug("Removing subject {}", subject);
        final boolean deleted = subjects.remove(subject);
        EventBusHolder.post(new ChangeEvent<>(subject, ChangeType.DELETE));
        return deleted;
    }

    public Node getActions() {
        return actions;
    }

    public boolean addAction(Action action) {
        if (actions.contains(action)) {
            log.debug("Won't setData another {}", action);
            return false;
        }

        log.debug("Adding action {}", action);
        actions.addChild(action);
        EventBusHolder.post(new ChangeEvent<>(action, ChangeType.CREATE));
        return true;
    }

    public boolean removeAction(Action action) {
        log.debug("Removing action {}", action);
        final boolean deleted = actions.remove(action);
        EventBusHolder.post(new ChangeEvent<>(action, ChangeType.DELETE));
        return deleted;
    }

    public Node getObservations() {
        return observations;
    }

    public boolean addObservation(Observation observation) {
        if (observations.contains(observation)) {
            log.debug("Won't setData another {}", observation);
            return false;
        }

        log.debug("Adding observation {}", observation);
        observations.addChild(observation);
        EventBusHolder.post(new ChangeEvent<>(observation, ChangeType.CREATE));
        return true;
    }

    public boolean addModifierFactory(ModifierFactory modifierFactory) {
        if (modifierFactories.contains(modifierFactory)) {
            log.debug("Won't setData another {}", modifierFactory);
            return false;
        }

        log.debug("Adding modifierFactory {}", modifierFactory);
        modifierFactories.addChild(modifierFactory);
        EventBusHolder.post(new ChangeEvent<>(modifierFactory, ChangeType.CREATE));
        return true;
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

    public File getSavePath() {
        return savePath;
    }

    public void setSavePath(File savePath) {
        this.savePath = savePath;
    }
}
