package org.obehave.persistence.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.model.modifier.Modifier;
import org.obehave.persistence.ModifierDao;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Markus Möslinger
 */
public class ModifierDaoImpl extends BaseDaoImpl<Modifier, Long> implements ModifierDao {
    public ModifierDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Modifier.class);
    }

    @Override
    public List<Modifier> queryForType(Modifier.Type type) throws SQLException {
        return queryBuilder().where().eq(Modifier.COLUMN_TYPE, type).query();
    }
}
