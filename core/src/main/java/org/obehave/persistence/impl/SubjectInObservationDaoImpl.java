package org.obehave.persistence.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.model.SubjectInObservation;
import org.obehave.persistence.SubjectInObservationDao;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class SubjectInObservationDaoImpl extends BaseDaoImpl<SubjectInObservation, Long> implements SubjectInObservationDao {
    public SubjectInObservationDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, SubjectInObservation.class);
    }
}
