package org.obehave.service;

import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
import org.obehave.exceptions.FactoryException;
import org.obehave.exceptions.ServiceException;
import org.obehave.exceptions.Validate;
import org.obehave.exceptions.ValidationException;
import org.obehave.model.*;
import org.obehave.persistence.Daos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Markus Möslinger
 */
public class CodingService implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(CodingService.class);

    public static class CodingServiceBuilder implements Serializable{
        private final Study study;
        private static final long serialVersionUID = 1L;

        public CodingServiceBuilder(Study study) {
            this.study = study;
        }

        public CodingService build(Observation observation) {
            return new CodingService(study, observation);
        }
    }

    private final Study study;
    private final Observation observation;
    private final List<Coding> openCodings = new ArrayList<>();

    private CodingService(Study study, Observation observation) {
        this.study = study;
        this.observation = observation;

        openCodings.addAll(observation.getOpenCodings());
    }

    public void initializeObservation() throws ServiceException {
        log.info("Initializing state codings for observation {}", observation);

        final List<Action> initialActions = getInitialActions();

        for (Displayable d : study.getSubjects().flatten()) {
            Subject s = (Subject) d;

            for (Action a : initialActions) {
                startCoding(s, a, null, 0);
            }
        }
    }

    private List<Action> getInitialActions() {
        List<Action> initialActions = new ArrayList<>();

        for (Node child : study.getActions().getChildren()) {
            if (child.getData() == null && !child.getChildren().isEmpty()) {
                initialActions.add(child.getInitialAction());
            }
        }

        return initialActions;
    }

    public Coding startCoding(String subject, String action, String modifierInput, long startMs) throws ServiceException {
        try {
            Validate.isNotEmpty(subject, "Subject");
            Validate.isNotEmpty(action, "Action");
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }

        final Subject s = study.getSubjectService().getForName(subject);
        final Action a = study.getActionService().getForName(action);

        return startCoding(s, a, modifierInput, startMs);
    }

    public Coding startCoding(Subject subject, Action action, String modifierInput, long startMs) throws ServiceException {
        try {
            Validate.isNotNull(subject, "Subject");
            Validate.isNotNull(action, "Action");
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }

        if (!observation.getParticipatingSubjects().contains(subject)) {
            throw new ServiceException("Subject " + subject.getDisplayString() + " isn't participating in this study!");
        }

        log.info("Starting coding for subject {}, action {}, input {} and start time {}", subject, action, modifierInput, startMs);

        Coding coding;

        try {
            if (action.getModifierFactory() != null) {
                coding = new Coding(subject, action, modifierInput, startMs);
            } else {
                coding = new Coding(subject, action, startMs);
            }

            // ending all open codings from the same group for the same subject
            // this could become a bottleneck. Keep care.
            Node actionParent = study.getActions().getParentOf(action);
            if (existsSubjectInOpenCodings(subject) && actionParent.getExclusivity() != Node.Exclusivity.NOT_EXCLUSIVE) {
                for (Coding openCoding : new ArrayList<>(openCodings)) {
                    Node codingParent = study.getActions().getParentOf(openCoding.getAction());
                    if (actionParent.equals(codingParent)
                            && coding.getSubject().equals(openCoding.getSubject())) {
                        // FIXME this won't work, I guess, because of modifier input
                        if (openCoding.getStartMs() <= startMs) {
                            endCoding(openCoding, startMs - 1);
                        }

                    }
                }
            }


            if (action.getType() == Action.Type.STATE) {
                openCodings.add(coding);
            }

            observation.addCoding(coding);

            Daos.get().coding().createOrUpdate(coding);
            study.getObservationService().save(observation);

            EventBusHolder.post(new UiEvent.NewCoding(coding));
        } catch (FactoryException e) {
            throw new ServiceException("Can't create coding with modifier " + modifierInput, e);
        } catch (SQLException e) {
            throw new ServiceException("Error while saving coding" + modifierInput, e);
        }

        return coding;
    }

    private Coding getOpenCoding(Subject subject, Action action, String modifierBuildString) {
        List<Coding> matchingCodings = new ArrayList<>();

        for (Coding openCoding : openCodings) {
            if (openCoding.getSubject().equals(subject) && openCoding.getAction().equals(action)) {
                matchingCodings.add(openCoding);
            }
        }

        if (matchingCodings.size() == 0) {
            return null;
        } else if (matchingCodings.size() == 1) {
            return matchingCodings.get(0);
        } else {
            return filterCodingsForModifier(matchingCodings, modifierBuildString);
        }
    }

    private Coding filterCodingsForModifier(List<Coding> codings, String buildString) {
        List<Coding> matchingCodings = new ArrayList<>();

        for (Coding coding : matchingCodings) {
            if (coding.getModifier().getBuildString().equals(buildString)) {
                matchingCodings.add(coding);
            }
        }

        if (matchingCodings.size() == 0) {
            return null;
        } else if (matchingCodings.size() == 1) {
            return matchingCodings.get(0);
        } else {
            throw new IllegalArgumentException("Foudn multiple matching codings for build string " + buildString);
        }
    }

    public void endCoding(String subject, String action, String modifierInput, long endMs) throws ServiceException {
        try {
            Validate.isNotEmpty(subject, "Subject");
            Validate.isNotEmpty(action, "Action");
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }

        final Subject s = study.getSubjectService().getForName(subject);
        final Action a = study.getActionService().getForName(action);

        endCoding(s, a, modifierInput, endMs);
    }

    public void endCoding(Subject subject, Action action, String modifierInput, long endMs) throws ServiceException {
        log.info("Stopping coding for subject {}, action {}, input {} and stop time {}", subject, action, modifierInput, endMs);

        try {
            Validate.isNotNull(subject, "Subject");
            Validate.isNotNull(action, "Action");
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }

        Coding coding = getOpenCoding(subject, action, modifierInput);

        if (coding == null) {
            throw new ServiceException("Couldn't find a running coding for subject " + subject.getDisplayString()
                    + " and action " + action.getDisplayString());
        }

        endCoding(coding, endMs);
    }

    public void endCoding(Coding coding, long endMs) throws ServiceException {
        if (endMs < coding.getStartMs()) {
            throw new ServiceException("Coding hasn't started yet!");
        }

        try {
            coding.setEndMs(endMs);
            Daos.get().coding().update(coding);
            openCodings.remove(coding);
            EventBusHolder.post(new UiEvent.FinishedCoding(coding));
        } catch (SQLException e) {
            throw new ServiceException("Couldn't update coding", e);
        }
    }

    private boolean existsSubjectInOpenCodings(Subject subject){
        for (Coding openCoding : new ArrayList<>(openCodings)) {
            if(openCoding.getSubject().equals(subject)){
                return true;
            }
        }

        return false;
    }

    public List<Coding> getOpenCodings() {
        return openCodings;
    }

    public void delete(Coding coding) throws ServiceException {
        try {
            Daos.get().coding().delete(coding);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new UiEvent.RepaintStudyTree());
    }
}
