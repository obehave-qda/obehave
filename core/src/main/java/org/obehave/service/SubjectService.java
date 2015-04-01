package org.obehave.service;

import org.obehave.events.EventBusHolder;
import org.obehave.events.RepaintStudyEvent;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Subject;
import org.obehave.persistence.Daos;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class SubjectService {
    private static final SubjectService instance = new SubjectService();

    private SubjectService() {

    }

    public static SubjectService getInstance() {
        return instance;
    }

    public void save(Subject s) throws ServiceException {
        try {
            Daos.get().subject().createOrUpdate(s);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new RepaintStudyEvent());
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
