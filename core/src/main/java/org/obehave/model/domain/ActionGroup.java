package org.obehave.model.domain;

/**
 * @author Markus Möslinger
 */
public class ActionGroup extends Group<Object> {
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
        TOTAL_EXCLUSIVE;
    }

    private Exclusivity exclusivity;
}
