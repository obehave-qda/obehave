package org.obehave.service;

import org.obehave.events.EventBusHolder;
import org.obehave.events.RepaintStudyEvent;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Action;
import org.obehave.persistence.Daos;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class ActionService {
    private static final ActionService instance = new ActionService();

    private ActionService() {

    }

    public static ActionService getInstance() {
        return instance;
    }

    public void save(Action action) throws ServiceException {
        try {
            Daos.get().action().createOrUpdate(action);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new RepaintStudyEvent());
    }

    public void delete(Action action) throws ServiceException {
        try {
            Daos.get().action().delete(action);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new RepaintStudyEvent());
    }
}
