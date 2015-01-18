package org.obehave.model.domain.coding;

import org.obehave.exceptions.FactoryException;
import org.obehave.model.domain.Action;
import org.obehave.model.domain.Subject;
import org.obehave.model.domain.modifier.Modifier;

/**
 * @author Markus Möslinger
 */
public class Coding {
    private Subject subject;
    private Action action;
    private Modifier modifier;
    private long startMs;

    public Coding(Subject subject, Action action, long startMs) {
        setSubject(subject);
        setAction(action);
        setStartMs(startMs);
    }

    public Coding(Subject subject, Action action, String modifierInput, long startMs) throws FactoryException {
        this(subject, action, startMs);
        setModifier(modifierInput);
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject must not be null!");
        }

        this.subject = subject;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        if (action == null) {
            throw new IllegalArgumentException("Action must not be null!");
        }
        this.action = action;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(String input) throws FactoryException {
        if (input == null) {
            throw new IllegalArgumentException("Input must not be null");
        }

        this.modifier = action.getModifierFactory().create(input);
    }

    public long getStartMs() {
        return startMs;
    }

    public void setStartMs(long startMs) {
        if (startMs <= 0) {
            throw new IllegalArgumentException("ms must not be lower or equal to 0");
        }

        this.startMs = startMs;
    }
}
