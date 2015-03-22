package org.obehave.service;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import org.obehave.events.ChangeEvent;
import org.obehave.events.ChangeType;
import org.obehave.events.EventBusHolder;
import org.obehave.exceptions.Validate;
import org.obehave.model.*;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.persistence.Daos;
import org.obehave.util.DatabaseProperties;
import org.obehave.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.SQLException;

/**
 * A study contains multiple subjects, actions and observations.
 */
public class Study implements Displayable {
    private static final Logger log = LoggerFactory.getLogger(Study.class);

    private String name;

    private Node subjects = new Node(Subject.class);
    private Node actions = new Node(Action.class);
    private Node observations = new Node(Observation.class);
    private Node modifierFactories = new Node(ModifierFactory.class);

    private File savePath;

    private Study() {

    }

    private Study(File savePath) {
        setSavePath(savePath);
    }

    @Deprecated
    public Study(String name) {
        this.name = name;
    }

    @Deprecated
    public static Study create() {
        log.info("Creating empty study");

        return new Study();
    }

    public static Study create(File savePath) throws SQLException {
        log.info("Creating new study at {}", savePath);

        final Study study = new Study(savePath);
        Daos.asDefault(new JdbcConnectionSource(Properties.getDatabaseConnectionStringWithInit(savePath)));

        study.subjects = Validate.hasOnlyOneElement(Daos.get().node().getRoot(Subject.class)).get(0);
        study.actions = Validate.hasOnlyOneElement(Daos.get().node().getRoot(Action.class)).get(0);
        study.modifierFactories = Validate.hasOnlyOneElement(Daos.get().node().getRoot(ModifierFactory.class)).get(0);
        study.observations = Validate.hasOnlyOneElement(Daos.get().node().getRoot(Observation.class)).get(0);

        return study;
    }

    public static Study load(File savePath) throws SQLException {
        log.info("Loading existing study from {}", savePath);

        final Study study = new Study(savePath);
        Daos.asDefault(new JdbcConnectionSource(Properties.getDatabaseConnectionString(savePath)));
        study.load();
        return study;
    }

    private void load() throws SQLException {
        log.info("Starting loading of entities");
        long start = System.currentTimeMillis();

        subjects = Validate.hasOnlyOneElement(Daos.get().node().getRoot(Subject.class)).get(0);
        actions = Validate.hasOnlyOneElement(Daos.get().node().getRoot(Action.class)).get(0);
        modifierFactories = Validate.hasOnlyOneElement(Daos.get().node().getRoot(ModifierFactory.class)).get(0);
        observations = Validate.hasOnlyOneElement(Daos.get().node().getRoot(Observation.class)).get(0);

        removeEmptyNodes(subjects);
        removeEmptyNodes(actions);
        removeEmptyNodes(modifierFactories);
        removeEmptyNodes(observations);

        final String studyName = DatabaseProperties.get(DatabaseProperties.STUDY_NAME);
        setName(studyName);

        long duration = System.currentTimeMillis() - start;
        log.info("Took {}ms for loading of entities", duration);
    }

    private void removeEmptyNodes(Node node) {
        if (node.getData() == null && (node.getTitle() == null || node.getTitle().isEmpty())) {
            node.getParent().remove(node);
            log.warn("Why is there an empty node at all? Look! " + node);
        } else {
            for (Node child : node.getChildren()) {
                removeEmptyNodes(child);
            }
        }
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

    public Node getModifierFactories() {
        return modifierFactories;
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
        log.debug("Setting study name to {}", name);
        this.name = name;
        DatabaseProperties.set(DatabaseProperties.STUDY_NAME, name);
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

    @Override
    public String getDisplayString() {
        return getName();
    }
}
