package org.obehave.model.domain.coding;

import org.obehave.exceptions.FactoryException;
import org.obehave.model.domain.Action;
import org.obehave.model.domain.Subject;

/**
 * @author Markus Möslinger
 */
public class PointCoding extends Coding {
    private long ms;

    public PointCoding(Subject subject, Action action, long ms) {
        super(subject, action);
        this.ms = ms;
    }

    public PointCoding(Subject subject, Action action, String modifierInput, long ms) throws FactoryException {
        super(subject, action, modifierInput);
        this.ms = ms;
    }

    public long getMs() {
        return ms;
    }

    public void setMs(long ms) {
        if (ms <= 0) {
            throw new IllegalArgumentException("ms must not be lower or equal to 0");
        }

        this.ms = ms;
    }
}
