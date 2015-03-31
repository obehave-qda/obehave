package org.obehave.service;

import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
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
        checkBeforeSave(mf);

        try {
            Daos.get().modifierFactory().createOrUpdate(mf);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new UiEvent.RepaintStudyTree());
    }

    @Override
    protected void checkBeforeSave(ModifierFactory mf) throws ServiceException {
        for (Displayable existingModifierFactory : getStudy().getModifierFactories().flatten()) {
            ModifierFactory existing = (ModifierFactory) existingModifierFactory;
            if (!existing.getId().equals(mf.getId()) &&
                    (existing.getName().equals(mf.getName()) || existing.getAlias().equals(mf.getAlias()))) {
                throw new ServiceException("Name and alias have to be unique! Found in: " +
                        existingModifierFactory.getDisplayString());
            }
        }
    }

    public void delete(ModifierFactory mf) throws ServiceException {
        try {
            Daos.get().modifierFactory().delete(mf);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new UiEvent.RepaintStudyTree());
    }
}
