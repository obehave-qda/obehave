package org.obehave.service;

import com.j256.ormlite.support.ConnectionSource;
import org.obehave.exceptions.DatabaseException;
import org.obehave.model.PropertyDto;
import org.obehave.persistence.Daos;
import org.obehave.persistence.PropertyDao;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class StudyPropertyService {
    public static final String STUDY_NAME = "study.name";

    private final PropertyDao dao;

    protected StudyPropertyService(ConnectionSource connection) {
        try {
            dao = Daos.get(connection).property();
        } catch (SQLException e) {
            throw new DatabaseException("Cannot create study property service", e);
        }
    }

    public String get(String property) {
        try {
            final PropertyDto dto = dao.getProperty(property);
            return dto != null ? dto.getValue() : null;
        } catch (SQLException e) {
            throw new DatabaseException("Couldn't retrieve property " + property, e);
        }
    }

    public void set(String property, String value) {
        try {
            dao.setOrUpdateProperty(property, value);
        } catch (SQLException e) {
            throw new DatabaseException("Couldn't set property " + property + " to value " + value, e);
        }
    }
}
