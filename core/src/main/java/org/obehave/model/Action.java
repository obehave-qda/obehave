package org.obehave.model;

import com.j256.ormlite.field.DatabaseField;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.obehave.model.modifier.ModifierFactory;

/**
 * This class describes actions a subject is able to perform.
 * There are several types of actions:
 * <p />
 * Point and state actions
 * Single actions or interactions between different subjects
 * <p />
 * Actions can be modified in some way.
 */
public class Action extends BaseEntity implements Displayable {
    public static enum Type {
        POINT, STATE
    }

    @DatabaseField
    private String name;
    private String alias;
    private Type type;
    private ModifierFactory<?> modifierFactory;
    private int recurring;


    public Action(){

    }

    public Action(String name) {
        this.name = name;
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

    public ModifierFactory<?> getModifierFactory() {
        return modifierFactory;
    }

    public void setModifierFactory(ModifierFactory<?> modifierFactory) {
        this.modifierFactory = modifierFactory;
    }

    public int getRecurring() {
        return recurring;
    }

    public void setRecurring(int recurring) {
        if (recurring < 0) {
            this.recurring = 0;
        } else {
            this.recurring = recurring;
        }
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

    public boolean isRecurring() {
        return recurring != 0;
    }
}
