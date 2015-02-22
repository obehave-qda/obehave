package org.obehave.persistence;

import com.j256.ormlite.dao.Dao;
import org.obehave.model.Observation;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public interface ObservationDao extends Dao<Observation, Long> {
    Observation queryForName(String name) throws SQLException;
}
