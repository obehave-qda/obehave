package org.obehave.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.persistence.impl.ActionDaoImpl;

/**
 * This class describes actions a subject is able to perform.
 * There are several types of actions:
 * <p/>
 * Point and state actions
 * Single actions or interactions between different subjects
 * <p/>
 * Actions can be modified in some way.
 */
@DatabaseTable(tableName = "Action", daoClass = ActionDaoImpl.class)
public class Action extends BaseEntity implements Displayable {
    public static final String COLUMN_NAME = "name";

    @DatabaseField(columnName = COLUMN_NAME)
    private String name;
    @DatabaseField(columnName = "alias")
    private String alias;
    @DatabaseField(columnName = "type")
    private Type type;
    @DatabaseField(columnName = "modifierFactory", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private ModifierFactory modifierFactory;
    @DatabaseField(columnName = "recurring")
    private int recurring;

    public Action() {
    }

    /**
     * Creates a point action
     * @param name the name of the action
     */
    public Action(String name) {
        this(name, Type.POINT);
    }

    public Action(String name, Type type) {
        setName(name);
        setType(type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ModifierFactory getModifierFactory() {
        return modifierFactory;
    }

    public void setModifierFactory(ModifierFactory modifierFactory) {
        this.modifierFactory = modifierFactory;
    }

    public int getRecurring() {
        return recurring;
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
        Action rhs = (Action) obj;

        return new EqualsBuilder().append(name, rhs.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("name", name).append("alias", alias).append("type", type).append("recurring", recurring)
                .append("modifierFactory", modifierFactory).toString();
    }

    public boolean isRecurring() {
        return recurring != 0;
    }

    public void setRecurring(int recurring) {
        if (recurring < 0) {
            throw new IllegalArgumentException("Recurring must not be negative");
        }

        this.recurring = recurring;
    }

    public static enum Type {
        POINT, STATE
    }
}
