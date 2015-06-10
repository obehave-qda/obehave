package org.obehave.persistence;

import com.j256.ormlite.dao.Dao;
import org.obehave.model.PropertyDto;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public interface PropertyDao extends Dao<PropertyDto, Long> {
    PropertyDto getProperty(String key) throws SQLException;

    void setOrUpdateProperty(String key, String value) throws SQLException;
}
