package org.obehave.model;

/**
 * This class describes actions a subject is able to perform.
 * There are several types of actions:
 * <p />
 * Point and state actions
 * Single actions or interactions between different subjects
 *<p />
 * Actions can be modified in some way.
 */
public class Action extends BaseEntity implements Displayable {
    private String name;

    public Action(String name) {
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
