package org.obehave.service;

import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Node;
import org.obehave.persistence.Daos;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class NodeService {
    private final Study study;

    protected NodeService(Study study) {
        this.study = study;
    }

    public void save(Node node) throws ServiceException {
        try {
            Daos.get().node().createOrUpdate(node);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new UiEvent.RepaintStudyTree());
    }

    public void delete(Node node) throws ServiceException {
        try {
            Daos.get().node().delete(node);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new UiEvent.RepaintStudyTree());
    }
}
