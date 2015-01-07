package org.obehave.model;

import com.j256.ormlite.field.DatabaseField;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @DatabaseField
    private String name;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "study_id")
    private Study study;

    @DatabaseField(foreign = true, canBeNull = true, columnName = "group_id")
    private ActionGroup actionGroup;

    public Action(){

    }

    public Action(String name) {
        this.name = name;
    }

    public void setStudy(Study study){
        this.study = study;
    }

    public Study getStudy(){
        return study;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ActionGroup getActionGroup() {
        return actionGroup;
    }

    public void setActionGroup(ActionGroup actionGroup) {
        this.actionGroup = actionGroup;
    }
}
