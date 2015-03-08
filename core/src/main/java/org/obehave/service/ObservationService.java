package org.obehave.service;

import org.obehave.events.EventBusHolder;
import org.obehave.events.RepaintStudyEvent;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Observation;
import org.obehave.persistence.Daos;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class ObservationService {
    private static final ObservationService instance = new ObservationService();

    private ObservationService() {

    }

    public static ObservationService getInstance() {
        return instance;
    }

    public void save(Observation o) throws ServiceException {
        try {
            Daos.get().observation().createOrUpdate(o);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new RepaintStudyEvent());
    }

    public void delete(Observation o) throws ServiceException {
        try {
            Daos.get().observation().delete(o);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new RepaintStudyEvent());
    }
}
