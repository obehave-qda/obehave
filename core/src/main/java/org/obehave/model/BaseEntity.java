package org.obehave.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

/**
 * Serves as a base class for all entities, which will be persisted into sql databases.
 * Provides basic functionality to determin new instances as well as support for optimistic locking.
 */
public class BaseEntity {

    /**
     * Represents the primary key for an object in the sql database.
     * Must be unique within a table, but NOT a subsequent sequence WITHOUT gaps
     */
    private Long id;

    /**
     * Serves as operand for optimistic locking - http://infos.zu.optimistic.locking.com
     */
    private Integer version = 0;

    /**
     * Marks the timestamp when the instance was created
     */
    private LocalDateTime creationTS;

    /**
     * Marks the timestamp when the instance was modified last
     */
    private LocalDateTime modifiedTS;

    /**
     * Indicates whether the instance was modified since the last read operation.
     * Used to optimize write operations towards the sql database.
     * Due to the transient keyword, we indicate, that we do not want it stored.
     */
    private transient boolean dirtyFlag;

    public BaseEntity() {
        // needed for compatibility
        this(LocalDateTime.now());
    }

    public BaseEntity(LocalDateTime creationTS) {
        setCreationTS(creationTS);
        markAsDirty();
    }

    public BaseEntity(Long id, Integer version, LocalDateTime creationTS) {
        this(creationTS); // delegate initialization of creationTS to other constructor
        setId(id);
        setVersion(version);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getVersion() {
        return version;
    }

    public void setCreationTS(LocalDateTime creationTS) {
        this.creationTS = creationTS;
    }

    public LocalDateTime getCreationTS() {
        return creationTS;
    }

    public void setModifiedTS(LocalDateTime modifiedTS) {
        this.modifiedTS = modifiedTS;
    }

    public void updateModifiedTS() {
        setModifiedTS(LocalDateTime.now());
    }

    public LocalDateTime getModifiedTS() {
        return modifiedTS;
    }

    /**
     * Exposes the behaviour/capability, but not the technical implementation details of the feature.
     * This will update the modified timestamp too.
     *
     * @return self-reference, which enables us to do method-chaining
     */
    public BaseEntity markAsDirty() {
        dirtyFlag = true;
        updateModifiedTS();
        return this;
    }

    public BaseEntity markAsClean() {
        dirtyFlag = false;
        return this;
    }

    public boolean isDirty() {
        return dirtyFlag;
    }

    public boolean isNew() {
        return id == null;
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
                .append("version", version)
                .append("creationTS", creationTS)
                .append("modifiedTS", modifiedTS)
                .append("dirty", dirtyFlag)
                .toString();
    }
}