package org.obehave.model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * This class describes the observed subjects of a study
 */
@Entity
public class Subject extends BaseEntity {
    @Column
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
