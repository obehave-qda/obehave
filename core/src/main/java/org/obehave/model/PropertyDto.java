package org.obehave.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.obehave.exceptions.Validate;
import org.obehave.persistence.impl.PropertyDaoImpl;

/**
 * @author Markus Möslinger
 */
@DatabaseTable(tableName = "Property", daoClass = PropertyDaoImpl.class)
public class PropertyDto extends BaseEntity {
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_VALUE = "value";

    @DatabaseField(columnName = COLUMN_KEY)
    private String key;

    @DatabaseField(columnName = COLUMN_VALUE)
    private String value;

    private PropertyDto() {
        // for frameworks
    }

    public PropertyDto(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public void setValue(String value) {
        Validate.isNotNull(value, "Value");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        PropertyDto rhs = (PropertyDto) obj;

        return new EqualsBuilder().append(key, rhs.key).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(key).build();
    }
}
