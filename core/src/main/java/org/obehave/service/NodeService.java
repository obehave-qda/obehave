package org.obehave.service;

import org.obehave.events.EventBusHolder;
import org.obehave.events.RepaintStudyEvent;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Node;
import org.obehave.persistence.Daos;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class NodeService {
    private static final NodeService instance = new NodeService();

    private NodeService() {

    }

    public static NodeService getInstance() {
        return instance;
    }

    public void save(Node node) throws ServiceException {
        try {
            Daos.get().node().createOrUpdate(node);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new RepaintStudyEvent());
    }

    public void delete(Node node) throws ServiceException {
        try {
            Daos.get().node().delete(node);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }

        EventBusHolder.post(new RepaintStudyEvent());
    }
}
