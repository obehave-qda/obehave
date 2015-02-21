package org.obehave.persistence.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.model.Action;
import org.obehave.persistence.ActionDao;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class ActionDaoImpl extends BaseDaoImpl<Action, Long> implements ActionDao {
    public ActionDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Action.class);
    }

    @Override
    public Action queryForName(String name) throws SQLException {
        return queryBuilder().where().eq(Action.COLUMN_NAME, name).queryForFirst();
    }
}
