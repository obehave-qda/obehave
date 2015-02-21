package org.obehave.persistence.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.model.Node;
import org.obehave.persistence.NodeDao;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class NodeDaoImpl extends BaseDaoImpl<Node, Long> implements NodeDao {
    public NodeDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Node.class);
    }
}
