package org.obehave.model;

import com.j256.ormlite.field.DatabaseField;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;


/**
 * Serves as a base class for all entities, which will be persisted into sql databases.
 * Provides basic functionality to determin new instances as well as support for optimistic locking.
 */
public class BaseEntity {

    /**
     * Represents the primary key for an object in the sql database.
     * Must be unique within a table, but NOT a subsequent sequence WITHOUT gaps
     */
    @DatabaseField(generatedId = true, columnName = "_id")
    private Long id;

    /**
     * Marks the timestamp when the instance was created
     */
    @DatabaseField
    private DateTime creationTS;

    /**
     * Marks the timestamp when the instance was modified last
     */
    @DatabaseField
    private DateTime modifiedTS;

    public BaseEntity() {
        setCreationTS(new DateTime());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCreationTS(DateTime creationTS) {
        this.creationTS = creationTS;
    }

    public DateTime getCreationTS() {
        return creationTS;
    }

    public DateTime getModifiedTS() {
        return modifiedTS;
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
        BaseEntity rhs = (BaseEntity) obj;

        return new EqualsBuilder().append(id, rhs.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).build();
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("creationTS", creationTS)
                .append("modifiedTS", modifiedTS)
                .toString();
    }
}