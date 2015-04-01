package org.obehave.service;

import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Observation;
import org.obehave.persistence.Daos;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class ObservationService extends BaseEntityService<Observation> {
    protected ObservationService(Study study) {
        super(study, study.getObservations());
    }

    public void save(Observation observation) throws ServiceException {
        checkBeforeSave(observation);

        try {
            Daos.get().observation().createOrUpdate(observation);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new UiEvent.RepaintStudyTree());
    }

    @Override
    protected void checkBeforeSave(Observation observation) throws ServiceException {
        for (Observation existing : getStudy().getObservationsList()) {
            if (!existing.getId().equals(observation.getId()) && (existing.getName().equals(observation.getName()))) {
                throw new ServiceException("Name has to be unique! Found in " + existing.getDisplayString());
            }
        }
    }

    public void delete(Observation o) throws ServiceException {
        try {
            Daos.get().observation().delete(o);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new UiEvent.RepaintStudyTree());
    }
}
