package org.obehave.model;

import org.obehave.exceptions.FactoryException;
import org.obehave.model.modifier.Modifier;

/**
 * @author Markus Möslinger
 */
public class Coding extends BaseEntity {
    private Subject subject;
    private Action action;
    private Modifier modifier;
    private long startMs;
    private long endMs = -1;

    public Coding(Subject subject, Action action, long startMs) {
        this(subject, action, startMs, 0);
    }

    public Coding(Subject subject, Action action, long startMs, long endMs) {
        setSubject(subject);
        setAction(action);
        setStartMs(startMs);
        setEndMs(endMs);
    }

    public Coding(Subject subject, Action action, String modifierInput, long startMs) throws FactoryException {
        this(subject, action, modifierInput, startMs, 0);
    }

    public Coding(Subject subject, Action action, String modifierInput, long startMs, long endMs) throws FactoryException {
        this(subject, action, startMs, endMs);
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
        if (action.getModifierFactory() == null) {
            throw new FactoryException("This action has no modifier factory!");
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

    public long getEndMs() {
        return endMs;
    }

    public void setEndMs(long endMs) {
        this.endMs = endMs;
    }

    public long getDuration() {
        validateStateCoding();

        return endMs - startMs;
    }

    public boolean isStateCoding() {
        return endMs > startMs;
    }

    private void validateStateCoding() {
        if (!isStateCoding()) {
            throw new IllegalStateException("Coding has to be a state coding!");
        }
    }
}
