package org.obehave.persistence.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.model.Property;
import org.obehave.persistence.PropertyDao;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class PropertyDaoImpl extends BaseDaoImpl<Property, Long> implements PropertyDao {
    public PropertyDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Property.class);
    }

    @Override
    public Property getProperty(String key) throws SQLException {
        return queryBuilder().where().eq(Property.COLUMN_KEY, key).queryForFirst();
    }

    @Override
    public void setOrUpdateProperty(String key, String value) throws SQLException {
        Property existingProperty = getProperty(key);
        if (existingProperty == null) {
            create(new Property(key, value));
        } else {
            if (!existingProperty.getValue().equals(value)) {
                existingProperty.setValue(value);
                update(existingProperty);
            }
        }
    }
}
