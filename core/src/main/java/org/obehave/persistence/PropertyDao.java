package org.obehave.persistence;

import com.j256.ormlite.dao.Dao;
import org.obehave.model.Property;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public interface PropertyDao extends Dao<Property, Long> {
    Property getProperty(String key) throws SQLException;

    void setOrUpdateProperty(String key, String value) throws SQLException;
}
