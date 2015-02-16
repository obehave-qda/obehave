package org.obehave.persistence.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.model.Observation;
import org.obehave.persistence.ObservationDao;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class ObservationDaoImpl extends BaseDaoImpl<Observation, Long> implements ObservationDao {
    public ObservationDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Observation.class);
    }
}
