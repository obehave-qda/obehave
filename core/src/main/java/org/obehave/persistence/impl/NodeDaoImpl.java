package org.obehave.persistence.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.model.Node;
import org.obehave.persistence.Daos;
import org.obehave.persistence.NodeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Collection;

/**
 * @author Markus Möslinger
 */
public class NodeDaoImpl extends BaseDaoImpl<Node, Long> implements NodeDao {
    private static final Logger log = LoggerFactory.getLogger(NodeDaoImpl.class);

    public NodeDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Node.class);
    }

    @Override
    public int create(Node data) throws SQLException {
        final int id = super.create(data);
        log.debug("Autocreate children of {}", data);
        autoCreateChildren(data);
        return id;
    }

    @SuppressWarnings("unchecked")
    private void autoCreateChildren(Node data) throws SQLException {
        try {
            Field childrenField = data.getClass().getDeclaredField("children");
            childrenField.setAccessible(true);
            Collection<Node> children = (Collection<Node>) childrenField.get(data);

            for (Node child : children) {
                Daos.node().create(child);
                log.trace("Created {}", child);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new SQLException("Couldn't create children", e);
        }
    }
}
