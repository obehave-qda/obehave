package org.obehave.service;

import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Action;
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

        EventBusHolder.post(new UiEvent.RepaintStudyTree());
    }

    @Override
    protected void checkBeforeSave(Action action) throws ServiceException {
        for (Action existing : getStudy().getActionList()) {
            if (!existing.getId().equals(action.getId())) {
                if (existing.getName().equalsIgnoreCase(action.getName())) {
                    throw new ServiceException("Name has to be unique! Already found in " + existing.getDisplayString());
                } else if (existing.getAlias() != null && !existing.getAlias().isEmpty() && existing.getAlias().equalsIgnoreCase(action.getAlias())) {
                    throw new ServiceException("Alias has to be unique! Already found in " + existing.getDisplayString());
                }
            }
        }
    }

    public void delete(Action action) throws ServiceException {
        try {
            Daos.get().action().delete(action);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new UiEvent.RepaintStudyTree());
    }
}
