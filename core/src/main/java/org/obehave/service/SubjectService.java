package org.obehave.service;

import org.obehave.events.EventBusHolder;
import org.obehave.events.RepaintStudyEvent;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Displayable;
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

        EventBusHolder.post(new RepaintStudyEvent());
    }

    @Override
    protected void checkBeforeSave(Subject subject) throws ServiceException {
        for (Displayable existingSubject : getStudy().getSubjects().flatten()) {
            Subject existing = (Subject) existingSubject;
            if (!existing.getId().equals(subject.getId()) &&
                    (existing.getName().equals(subject.getName()) || existing.getAlias().equals(subject.getAlias()))) {
                throw new ServiceException("Name and alias have to be unique! Found in: " + existingSubject.getDisplayString());
            }
        }
    }

    public void delete(Subject s) throws ServiceException {
        try {
            Daos.get().subject().delete(s);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new RepaintStudyEvent());
    }
}
