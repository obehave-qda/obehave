package org.obehave.model.domain;

/**
 * @author Markus Möslinger
 */
public class ActionGroup extends Group<Action> {
    public static enum Exclusivity {
        /**
         * Multiple state actions are allowed at the same time
         */
        NOT_EXCLUSIVE,
        /**
         * Not recursive - only one element (regardless if subgroup or action) is allowed to be/contain active state actions
         */
        EXCLUSIVE,
        /**
         * Recursive - only one element is allowed to be active
         */
        TOTAL_EXCLUSIVE
    }

    private Exclusivity exclusivity;

    public ActionGroup(Exclusivity exclusivity) {
        this.exclusivity = exclusivity;
    }

    public Exclusivity getExclusivity() {
        return exclusivity;
    }

    public void setExclusivity(Exclusivity exclusivity) {
        if (exclusivity == null) {
            throw new IllegalArgumentException("Exclusivity must not be null!");
        }

        this.exclusivity = exclusivity;
    }
}
