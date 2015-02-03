package org.obehave.model.tree;

import com.j256.ormlite.field.DatabaseField;
import org.obehave.model.Action;

/**
 * @author Markus Möslinger
 */
public class ActionNode extends Node<Action> {
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

    @DatabaseField(columnName = "actionType")
    private Exclusivity exclusivity;

    public ActionNode(Exclusivity exclusivity) {
        super(Action.class);
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
