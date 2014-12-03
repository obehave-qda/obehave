package org.obehave.model;

/**
 * During an observation, it's possible to code subjects and actions.
 */
public class Observation extends BaseEntity implements Displayable {
    private String name;

    public Observation(String name) {
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
