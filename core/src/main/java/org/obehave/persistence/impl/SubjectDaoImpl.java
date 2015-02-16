package org.obehave.persistence.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.model.Subject;
import org.obehave.persistence.SubjectDao;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class SubjectDaoImpl extends BaseDaoImpl<Subject, Long> implements SubjectDao {
    public SubjectDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Subject.class);
    }
}
