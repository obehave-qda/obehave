package org.obehave.service;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
import org.obehave.exceptions.DatabaseException;
import org.obehave.exceptions.Validate;
import org.obehave.model.*;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.persistence.Daos;
import org.obehave.util.DatabaseProperties;
import org.obehave.util.FileUtil;
import org.obehave.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

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

    private SuggestionService.SuggestionServiceBuilder suggestionServiceBuilder;
    private ActionService actionService;
    private NodeService nodeService;
    private ModifierFactoryService modifierFactoryService;
    private ObservationService observationService;
    private SubjectService subjectService;
    private CodingService.CodingServiceBuilder codingServiceBuilder;

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

    // TODO throw ServiceException instead of SQLException
    public static Study create(File savePath) throws SQLException {
        log.info("Creating new study at {}", savePath);

        if (savePath.exists()) {
            if (!FileUtil.isDatabaseFileLocked(savePath)) {
                log.info("File {} exists already, creating new one at same path", savePath);
                if (!savePath.delete()) {
                    log.error("Couldn't delete file {}!", savePath);
                }
            } else {
                throw new DatabaseException("Cannot override locked database file");
            }
        }

        final Study study = new Study(savePath);
        Daos.asDefault(new JdbcConnectionSource(Properties.getDatabaseConnectionStringWithInit(savePath)));

        study.subjects = Validate.hasOnlyOneElement(Daos.get().node().getRoot(Subject.class)).get(0);
        study.actions = Validate.hasOnlyOneElement(Daos.get().node().getRoot(Action.class)).get(0);
        study.modifierFactories = Validate.hasOnlyOneElement(Daos.get().node().getRoot(ModifierFactory.class)).get(0);
        study.observations = Validate.hasOnlyOneElement(Daos.get().node().getRoot(Observation.class)).get(0);

        return study;
    }

    // TODO throw ServiceException instead of SQLException
    public static Study load(File savePath) throws SQLException {
        log.info("Loading existing study from {}", savePath);

        final Study study = new Study(savePath);
        Daos.asDefault(new JdbcConnectionSource(Properties.getDatabaseConnectionString(savePath)));
        study.load();
        return study;
    }

    private void load() throws SQLException {
        log.info("Starting loading of entities");
        long startLoad = System.currentTimeMillis();

        // we want to load a single value first to establish a database connection
        name = DatabaseProperties.get(DatabaseProperties.STUDY_NAME);
        final long startEntities = System.currentTimeMillis();

        subjects = Validate.hasOnlyOneElement(Daos.get().node().getRoot(Subject.class)).get(0);
        actions = Validate.hasOnlyOneElement(Daos.get().node().getRoot(Action.class)).get(0);
        modifierFactories = Validate.hasOnlyOneElement(Daos.get().node().getRoot(ModifierFactory.class)).get(0);
        observations = Validate.hasOnlyOneElement(Daos.get().node().getRoot(Observation.class)).get(0);

        removeEmptyNodes(subjects);
        removeEmptyNodes(actions);
        removeEmptyNodes(modifierFactories);
        removeEmptyNodes(observations);

        final long durationEntities = System.currentTimeMillis() - startEntities;
        final long durationLoad = System.currentTimeMillis() - startLoad;
        log.info("Took {}ms for loading of entities ({}ms in total for loading)", durationEntities, durationLoad);
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

    public List<Subject> getSubjectsList() {
        return subjects.flattenAs(Subject.class);
    }

    public Node getActions() {
        return actions;
    }

    public List<Action> getActionList() {
        return actions.flattenAs(Action.class);
    }

    public Node getObservations() {
        return observations;
    }

    public List<Observation> getObservationsList() {
        return observations.flattenAs(Observation.class);
    }

    public Node getModifierFactories() {
        return modifierFactories;
    }

    public List<ModifierFactory> getModifierFactoryList() {
        return modifierFactories.flattenAs(ModifierFactory.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        log.debug("Setting study name to {}", name);
        this.name = name;
        DatabaseProperties.set(DatabaseProperties.STUDY_NAME, name);

        EventBusHolder.post(new UiEvent.RepaintStudyTree());
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

    public SuggestionService.SuggestionServiceBuilder getSuggestionServiceBuilder() {
        if (suggestionServiceBuilder == null) {
            suggestionServiceBuilder = new SuggestionService.SuggestionServiceBuilder(this);
        }

        return suggestionServiceBuilder;
    }

    public ActionService getActionService() {
        if (actionService == null) {
            actionService = new ActionService(this);
        }

        return actionService;
    }

    public ObservationService getObservationService() {
        if (observationService == null) {
            observationService = new ObservationService(this);
        }

        return observationService;
    }

    public ModifierFactoryService getModifierFactoryService() {
        if (modifierFactoryService == null) {
            modifierFactoryService = new ModifierFactoryService(this);
        }

        return modifierFactoryService;
    }

    public NodeService getNodeService() {
        if (nodeService == null) {
            nodeService = new NodeService(this);
        }

        return nodeService;
    }

    public SubjectService getSubjectService() {
        if (subjectService == null) {
            subjectService = new SubjectService(this);
        }

        return subjectService;
    }

    public CodingService.CodingServiceBuilder getCodingServiceBuilder() {
        if (codingServiceBuilder == null) {
            codingServiceBuilder = new CodingService.CodingServiceBuilder(this);
        }

        return codingServiceBuilder;
    }
}
