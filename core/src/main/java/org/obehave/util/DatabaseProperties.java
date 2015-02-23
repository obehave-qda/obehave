package org.obehave.util;

import org.obehave.exceptions.DatabaseUnavailableException;
import org.obehave.model.Property;
import org.obehave.persistence.Daos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class DatabaseProperties {
    private static final Logger log = LoggerFactory.getLogger(DatabaseProperties.class);

    public static final String STUDY_NAME = "study.name";

    private DatabaseProperties() {
        throw new AssertionError(I18n.getString("exception.constructor.utility"));
    }

    public static String get(String key) {
        try {
            final Property property = Daos.get().property().getProperty(key);
            return property == null ? null : property.getValue();
        } catch (SQLException e) {
            throw new DatabaseUnavailableException(I18n.get("exception.database.property.read", key), e);
        }
    }

    public static void set(String key, String value) {
        try {
            Daos.get().property().setOrUpdateProperty(key, value);
        } catch (SQLException e) {
            throw new DatabaseUnavailableException(I18n.get("exception.database.property.write", key, value), e);
        }
    }
}
