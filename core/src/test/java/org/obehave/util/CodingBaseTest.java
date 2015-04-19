package org.obehave.util;

import org.obehave.model.Action;
import org.obehave.model.Coding;
import org.obehave.model.Subject;

/**
 * @author Markus Möslinger
 */
public class CodingBaseTest {
    private Subject subject = new Subject("Dummysubject");
    private Action pointAction = new Action("Point Action", Action.Type.POINT);
    private Action stateAction = new Action("State Action", Action.Type.STATE);

    /**
     * Creates a new point coding
     * @param startMs startMs of the point coding
     * @return a new Coding instance
     */
    protected Coding pointCoding(int startMs) {
        return new Coding(subject, pointAction, startMs);
    }

    /**
     * Creates a new state coding
     * @param startMs startMs of the state coding
     * @return a new Coding instance
     */
    protected Coding stateCoding(int startMs) {
        return new Coding(subject, stateAction, startMs);
    }

    /**
     * Creates a new state coding
     * @param startMs startMs of the state coding
     * @param endMs endMs of the state coding
     * @return a new Coding instance
     */
    protected Coding stateCoding(int startMs, int endMs) {
        return new Coding(subject, stateAction, startMs, endMs);
    }
}
