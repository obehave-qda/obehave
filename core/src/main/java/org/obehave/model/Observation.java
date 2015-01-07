package org.obehave.model;

import com.j256.ormlite.field.DatabaseField;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * During an observation, it's possible to code subjects and actions.
 */
public class Observation extends BaseEntity implements Displayable {

    @DatabaseField
    private String name;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "study_id")
    private Study study;

    public Observation(){

    }

    public Observation(String name) {
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
        Observation rhs = (Observation) obj;

        return new EqualsBuilder().append(name, rhs.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).build();
    }
}
