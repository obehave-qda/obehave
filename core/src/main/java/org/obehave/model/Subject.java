package org.obehave.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.obehave.persistence.impl.SubjectDaoImpl;

/**
 * This class describes the observed subjects of a study
 */
@DatabaseTable(tableName = "Subject", daoClass = SubjectDaoImpl.class)
public class Subject extends BaseEntity implements Displayable {
    public static final String COLUMN_NAME = "name";

    @DatabaseField(columnName = COLUMN_NAME)
    private String name;

    @DatabaseField(columnName = "alias")
    private String alias;

    @DatabaseField(columnName = "color")
    private Color color;

    public Subject() {
    }

    public Subject(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null!");
        }

        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (color == null) {
            color = new Color(0);
        }

        this.color = color;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String getDisplayString() {
        return getName();
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
        Subject rhs = (Subject) obj;

        return new EqualsBuilder().append(name, rhs.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("name", name).append("alias", alias).append("color", color).toString();
    }
}
