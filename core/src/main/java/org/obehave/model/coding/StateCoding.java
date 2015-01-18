package org.obehave.model.coding;

import org.obehave.exceptions.FactoryException;
import org.obehave.model.Action;
import org.obehave.model.Subject;

/**
 * @author Markus Möslinger
 */
public class StateCoding extends Coding {
    private long endMs = -1;

    public StateCoding(Subject subject, Action action, long startMs, long endMs) {
        super(subject, action, startMs);
        setEndMs(endMs);
    }

    public StateCoding(Subject subject, Action action, String modifierInput, long startMs, long endMs) throws FactoryException {
        this(subject, action, startMs, endMs);
        setModifier(modifierInput);
    }

    public long getEndMs() {
        return endMs;
    }

    public void setEndMs(long endMs) {
        this.endMs = endMs;
    }

    public long getDuration() {
        if (endMs >= getStartMs()) {
            return endMs - getStartMs();
        } else {
            return -1;
        }
    }
}
