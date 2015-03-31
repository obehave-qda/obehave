package org.obehave.service;

import org.obehave.events.EventBusHolder;
import org.obehave.events.RepaintStudyEvent;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Displayable;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.persistence.Daos;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class ModifierFactoryService extends BaseEntityService<ModifierFactory> {
    protected ModifierFactoryService(Study study) {
        super(study, study.getModifierFactories());
    }

    public void save(ModifierFactory mf) throws ServiceException {
        try {
            Daos.get().modifierFactory().createOrUpdate(mf);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new RepaintStudyEvent());
    }

    @Override
    protected void checkBeforeSave(ModifierFactory mf) throws ServiceException {
        for (Displayable existingSubject : getStudy().getModifierFactories().flatten()) {
            ModifierFactory existing = (ModifierFactory) existingSubject;
            if (existing.getName().equals(mf.getName()) || existing.getAlias().equals(mf.getAlias())) {
                throw new ServiceException("Name and alias have to be unique!");
            }
        }
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
