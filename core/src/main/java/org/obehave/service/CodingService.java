package org.obehave.service;

import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
import org.obehave.exceptions.FactoryException;
import org.obehave.exceptions.ServiceException;
import org.obehave.exceptions.Validate;
import org.obehave.exceptions.ValidationException;
import org.obehave.model.*;
import org.obehave.persistence.Daos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Markus Möslinger
 */
public class CodingService {
    public static class CodingServiceBuilder {
        private final Study study;
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
    }

    public void initializeObservation() throws ServiceException {
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

        Coding coding;

        try {
            if (action.getModifierFactory() != null) {
                coding = new Coding(subject, action, modifierInput, startMs);
            } else {
                coding = new Coding(subject, action, startMs);
            }

            if (action.getType() == Action.Type.STATE) {
                openCodings.add(coding);
            }

            // ending all open codings from the same group for the same subject
            // this could become a bottleneck. Keep care.
            Node actionParent = study.getActions().getParentOf(action);
            if (actionParent.getExclusivity() != Node.Exclusivity.NOT_EXCLUSIVE) {
                for (Coding openCoding : new ArrayList<>(openCodings)) {
                    Node codingParent = study.getActions().getParentOf(openCoding.getAction());
                    if (actionParent.equals(codingParent)) {
                        endCoding(subject, action, startMs);
                    }
                }
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

    private Coding getOpenCoding(Subject subject, Action action) {
        for (Coding openCoding : openCodings) {
            if (openCoding.getSubject().equals(subject) && openCoding.getAction().equals(action)) {
                return openCoding;
            }
        }

        return null;
    }

    public void endCoding(Subject subject, Action action, long endMs) throws ServiceException {
        Coding coding = getOpenCoding(subject, action);

        if (coding == null) {
            throw new ServiceException("Couldn't find a running coding for subject " + subject.getDisplayString()
             + " and action " + action.getDisplayString());
        }

        try {
            Daos.get().coding().update(coding);
            openCodings.remove(coding);
            EventBusHolder.post(new UiEvent.FinishedCoding(coding));
        } catch (SQLException e) {
            throw new ServiceException("Couldn't update coding", e);
        }
    }

    public List<Coding> getOpenCodings() {
        return openCodings;
    }

    public long getEndOfLastCoding() {
        long max = 0;

        for (Coding coding : observation.getCodings()) {
            if (coding.getEndMs() > max) {
                max = coding.getEndMs();
            }
        }

        return max;
    }
}
