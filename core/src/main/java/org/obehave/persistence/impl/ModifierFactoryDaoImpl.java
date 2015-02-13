package org.obehave.persistence.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.persistence.ModifierFactoryDao;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class ModifierFactoryDaoImpl extends BaseDaoImpl<ModifierFactory, Long> implements ModifierFactoryDao {
    public ModifierFactoryDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, ModifierFactory.class);
    }
}
