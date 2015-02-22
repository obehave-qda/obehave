package org.obehave.persistence;

import com.j256.ormlite.dao.Dao;
import org.obehave.model.Node;

import java.sql.SQLException;
import java.util.List;

public interface NodeDao extends Dao<Node, Long> {
    /**
     * Retrieves all nodes of a certain type without parents. Usually this should only return one item,
     * but it's possible that independent nodes of the same type were stored.
     * It's reasonable to throw an exception in higher layers if this is the case.
     * @param contentType the class that is stored in the queried root node
     * @return a List of all root nodes. Under normal circumstances, the list should only contain one item.
     * @throws SQLException if something went wrong with the database
     */
    List<Node> getRoot(Class<?> contentType) throws SQLException;
}
