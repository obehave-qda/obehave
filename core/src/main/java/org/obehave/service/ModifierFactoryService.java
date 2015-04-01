package org.obehave.service;

import org.obehave.events.EventBusHolder;
import org.obehave.events.RepaintStudyEvent;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.persistence.Daos;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class ModifierFactoryService {
    private static final ModifierFactoryService instance = new ModifierFactoryService();

    private ModifierFactoryService() {

    }

    public static ModifierFactoryService getInstance() {
        return instance;
    }

    public void save(ModifierFactory mf) throws ServiceException {
        try {
            Daos.get().modifierFactory().createOrUpdate(mf);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new RepaintStudyEvent());
    }

    public void delete(ModifierFactory mf) throws ServiceException {
        try {
            Daos.get().modifierFactory().delete(mf);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new RepaintStudyEvent());
    }
}
