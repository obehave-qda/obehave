package org.obehave.persistence.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.model.PropertyDto;
import org.obehave.persistence.PropertyDao;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class PropertyDaoImpl extends BaseDaoImpl<PropertyDto, Long> implements PropertyDao {
    public PropertyDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, PropertyDto.class);
    }

    @Override
    public PropertyDto getProperty(String key) throws SQLException {
        return queryBuilder().where().eq(PropertyDto.COLUMN_KEY, key).queryForFirst();
    }

    @Override
    public void setOrUpdateProperty(String key, String value) throws SQLException {
        PropertyDto existingProperty = getProperty(key);
        if (existingProperty == null) {
            create(new PropertyDto(key, value));
        } else {
            if (!existingProperty.getValue().equals(value)) {
                existingProperty.setValue(value);
                update(existingProperty);
            }
        }
    }
}
