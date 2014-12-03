package org.obehave.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * This class describes the observed subjects of a study
 */
@DatabaseTable
public class Subject extends BaseEntity implements Displayable {
    @DatabaseField
    private String name;

    public Subject(String name) {
        this.name = name;
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
}
