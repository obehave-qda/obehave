package org.obehave.persistence;

import com.j256.ormlite.dao.Dao;
import org.obehave.model.Action;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public interface ActionDao extends Dao<Action, Long> {
    Action queryForName(String name) throws SQLException;
}
