package org.obehave.service;

import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Subject;
import org.obehave.persistence.Daos;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class SubjectService extends BaseEntityService<Subject> {
    protected SubjectService(Study study) {
        super(study, study.getSubjects());
    }

    public void save(Subject subject) throws ServiceException {
        checkBeforeSave(subject);

        try {
            Daos.get().subject().createOrUpdate(subject);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new UiEvent.RepaintStudyTree());
    }

    @Override
    protected void checkBeforeSave(Subject subject) throws ServiceException {
        for (Subject existing : getStudy().getSubjectsList()) {
            if (!existing.getId().equals(subject.getId())) {
                if (existing.getName().equals(subject.getName())) {
                    throw new ServiceException("Name has to be unique! Already found in " + existing.getDisplayString());
                } else if (existing.getAlias() != null && !existing.getAlias().isEmpty() && existing.getAlias().equals(subject.getAlias())) {
                    throw new ServiceException("Alias has to be unique! Already found in " + existing.getDisplayString());
                }
            }
        }
    }

    public void delete(Subject s) throws ServiceException {
        try {
            Daos.get().subject().delete(s);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new UiEvent.RepaintStudyTree());
    }
}
