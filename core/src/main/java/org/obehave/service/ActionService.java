package org.obehave.service;

import org.obehave.events.EventBusHolder;
import org.obehave.events.RepaintStudyEvent;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Action;
import org.obehave.model.Displayable;
import org.obehave.persistence.Daos;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class ActionService extends BaseEntityService<Action> {
    protected ActionService(Study study) {
        super(study, study.getActions());
    }

    public void save(Action action) throws ServiceException {
        checkBeforeSave(action);

        try {
            Daos.get().action().createOrUpdate(action);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new RepaintStudyEvent());
    }

    @Override
    protected void checkBeforeSave(Action action) throws ServiceException {
        for (Displayable existingAction : getStudy().getActions().flatten()) {
            Action existing = (Action) existingAction;
            if (existing.getName().equals(action.getName()) || existing.getAlias().equals(action.getAlias())) {
                throw new ServiceException("Name and alias have to be unique!");
            }
        }
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
