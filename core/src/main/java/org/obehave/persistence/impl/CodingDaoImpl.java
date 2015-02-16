package org.obehave.persistence.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.model.Coding;
import org.obehave.persistence.CodingDao;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class CodingDaoImpl extends BaseDaoImpl<Coding, Long> implements CodingDao {
    public CodingDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Coding.class);
    }
}
