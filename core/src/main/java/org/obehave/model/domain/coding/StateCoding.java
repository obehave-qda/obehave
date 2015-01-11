package org.obehave.model.domain.coding;

import org.obehave.exceptions.FactoryException;
import org.obehave.model.domain.Action;
import org.obehave.model.domain.Subject;

/**
 * @author Markus Möslinger
 */
public class StateCoding extends Coding {
    private long startMs;
    private long endMs;

    public StateCoding(Subject subject, Action action, long startMs, long endMs) {
        super(subject, action);
        this.startMs = startMs;
        this.endMs = endMs;
    }

    public StateCoding(Subject subject, Action action, String modifierInput, long startMs, long endMs) throws FactoryException {
        super(subject, action, modifierInput);
        this.startMs = startMs;
        this.endMs = endMs;
    }

    public long getStartMs() {
        return startMs;
    }

    public void setStartMs(long startMs) {
        if (startMs <= 0) {
            throw new IllegalArgumentException("startMs must be positive!");
        }

        this.startMs = startMs;
    }

    public long getEndMs() {
        return endMs;
    }

    public void setEndMs(long endMs) {
        if (endMs <= 0) {
            throw new IllegalArgumentException("endMs must be positive!");
        }

        this.endMs = endMs;
    }
}
