package org.obehave.persistence.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.model.modifier.ValidSubject;
import org.obehave.persistence.ValidSubjectDao;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class ValidSubjectDaoImpl extends BaseDaoImpl<ValidSubject, Long> implements ValidSubjectDao {
    public ValidSubjectDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, ValidSubject.class);
    }
}
