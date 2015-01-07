package org.obehave.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This class describes the observed subjects of a study
 */
@DatabaseTable
public class Subject extends BaseEntity implements Displayable {
    @DatabaseField
    private String name;

    @DatabaseField(foreign = true, columnName = "study_id")
    private Study study;

    @DatabaseField(foreign = true, canBeNull = true, columnName = "group_id")
    private SubjectGroup subjectGroup;

    public Subject() {

    }

    public Subject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    public Study getStudy() {
        return study;
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
        Subject rhs = (Subject) obj;

        return new EqualsBuilder().append(name, rhs.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).build();
    }
}
