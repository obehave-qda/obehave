package org.obehave.persistence.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.model.modifier.EnumerationItem;
import org.obehave.persistence.EnumerationItemDao;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class EnumerationItemDaoImpl extends BaseDaoImpl<EnumerationItem, Long> implements EnumerationItemDao {
    public EnumerationItemDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, EnumerationItem.class);
    }
}
