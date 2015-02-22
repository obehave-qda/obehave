package org.obehave.persistence;

import com.j256.ormlite.dao.Dao;
import org.obehave.model.Subject;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public interface SubjectDao extends Dao<Subject, Long> {
    Subject queryForName(String name) throws SQLException;
}
