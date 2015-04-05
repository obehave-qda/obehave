package org.obehave.util;

import org.junit.Before;
import org.junit.Test;
import org.obehave.model.Action;
import org.obehave.model.Coding;
import org.obehave.model.Subject;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CodingRangeTest {
    private Subject subject = new Subject("Dummysubject");
    private Action pointAction = new Action("Point Action", Action.Type.POINT);
    private Action stateAction = new Action("State Action", Action.Type.STATE);

    private CodingRange codingRange;

    @Before
    public void prepare() {
        codingRange = new CodingRange();
    }

    @Test
    public void pointCodingOverlapsPointCoding() {
        final Coding coding = pointCoding(5);
        coding.setSubject(new Subject("We don't want equal point codings"));
        codingRange.addOrUpdate(coding);

        assertEquals(1, codingRange.overlapCount(pointCoding(5)));
    }

    @Test
    public void pointCodingOverlapsStateCoding() {
        codingRange.addOrUpdate(stateCoding(0, 10));

        assertEquals(1, codingRange.overlapCount(pointCoding(5)));
    }

    @Test
    public void stateCodingOverlapsPointCoding() {
        codingRange.addOrUpdate(pointCoding(5));

        assertEquals(1, codingRange.overlapCount(stateCoding(0, 10)));
    }

    @Test
    public void stateCodingOverlapsStateCoding() {
        codingRange.addOrUpdate(stateCoding(0, 10));

        assertEquals(1, codingRange.overlapCount(stateCoding(5, 15)));
    }

    @Test
    public void stateCodingOverlapsOpenStateCoding() {
        codingRange.addOrUpdate(stateCoding(5));

        assertEquals(1, codingRange.overlapCount(stateCoding(7, 15)));
    }

    @Test
    public void pointCodingOverlapsOpenStateCoding() {
        codingRange.addOrUpdate(stateCoding(5));

        assertEquals(1, codingRange.overlapCount(pointCoding(7)));
    }

    @Test
    public void futureStateCodingDoesntOverlapStateCoding() {
        codingRange.addOrUpdate(stateCoding(5));

        assertEquals(0, codingRange.overlapCount(stateCoding(10, 15), 7));
    }

    @Test
    public void futurePointCodingDoesntOverlapStateCoding() {
        codingRange.addOrUpdate(stateCoding(5));

        assertEquals(0, codingRange.overlapCount(pointCoding(10), 7));
        assertEquals(1, codingRange.overlappingCodings(pointCoding(10), 7).getFutureOverlaps().size());
    }

    @Test
    public void stateCodingWillOverlapPointCodingInTheFuture() {
        codingRange.addOrUpdate(pointCoding(10));

        assertEquals(0, codingRange.overlapCount(stateCoding(5), 7));
        assertEquals(1, codingRange.overlappingCodings(stateCoding(5), 7).getFutureOverlaps().size());
    }

    @Test
    public void pointCodingDoesntOverlapPointCoding() {
        codingRange.addOrUpdate(pointCoding(5));

        assertEquals(0, codingRange.overlapCount(pointCoding(10)));
    }

    @Test
    public void pointCodingDoesntOverlapStateCoding() {
        codingRange.addOrUpdate(stateCoding(0, 5));

        assertEquals(0, codingRange.overlapCount(pointCoding(10)));
    }

    @Test
    public void stateCodingDoesntOverlapPointCoding() {
        codingRange.addOrUpdate(pointCoding(5));

        assertEquals(0, codingRange.overlapCount(stateCoding(10, 15)));
    }

    @Test
    public void stateCodingDoesntOverlapStateCoding() {
        codingRange.addOrUpdate(stateCoding(0, 5));

        assertEquals(0, codingRange.overlapCount(stateCoding(10, 15)));
    }

    @Test
    public void pointCodingOverlapsTwoCodings() {
        codingRange.addOrUpdate(pointCoding(0)); // no overlapping
        codingRange.addOrUpdate(stateCoding(0, 10)); // overlapping
        codingRange.addOrUpdate(stateCoding(3, 11)); // overlapping

        assertEquals(2, codingRange.overlapCount(pointCoding(5)));
    }

    @Test
    public void stateCodingOverlapsTwoCodings() {
        codingRange.addOrUpdate(pointCoding(0)); // no overlapping
        codingRange.addOrUpdate(stateCoding(0, 10)); // overlapping
        codingRange.addOrUpdate(stateCoding(3, 11)); // overlapping

        assertEquals(2, codingRange.overlapCount(stateCoding(4, 15)));
    }

    @Test
    public void pointCodingOverlapsNowClosedStateCoding() {
        final Coding coding = stateCoding(0);
        codingRange.addOrUpdate(coding);

        assertEquals(0, codingRange.overlapCount(pointCoding(5)));

        coding.setEndMs(10);
        assertEquals(0, codingRange.overlapCount(pointCoding(5)));
        codingRange.addOrUpdate(coding);

        assertEquals(1, codingRange.overlapCount(pointCoding(5)));
    }

    @Test
    public void codingDoesntOverlapItself() {
        final Coding coding = pointCoding(5);

        codingRange.addOrUpdate(coding);

        assertEquals(0, codingRange.overlapCount(coding));
    }

    @Test
    public void adjacentStateCodingsDontOverlap() {
        codingRange.addOrUpdate(stateCoding(0, 10));

        assertEquals(0, codingRange.overlapCount(stateCoding(10, 20)));
    }

    @Test
    public void adjacentPointCodingsDontOverlap() {
        codingRange.addOrUpdate(pointCoding(5));

        assertEquals(0, codingRange.overlapCount(pointCoding(6)));
    }

    @Test
    public void clearRemovesEntries() {
        codingRange.addOrUpdate(pointCoding(0)); // no overlapping
        codingRange.addOrUpdate(stateCoding(0, 10)); // overlapping
        codingRange.addOrUpdate(stateCoding(3, 11)); // overlapping

        assertEquals(2, codingRange.overlapCount(pointCoding(5)));

        codingRange.clear();

        assertEquals(0, codingRange.overlapCount(pointCoding(5)));
    }

    @Test
    public void arrangingOneStateAndThreePointActions() {
        Coding l1c1 = stateCoding(0, 20);
        Coding l2c1 = pointCoding(5);
        Coding l2c2 = pointCoding(10);
        Coding l2c3 = pointCoding(15);

        codingRange.addOrUpdate(l2c1);
        codingRange.addOrUpdate(l2c2);
        codingRange.addOrUpdate(l2c3);

        final List<List<Coding>> lanes = codingRange.overlappingCodings(l1c1, 30).arrangeCurrentOverlaps();
        assertTrue(lanes.get(0).contains(l1c1));
        assertTrue(lanes.get(1).containsAll(Arrays.asList(l2c1, l2c2, l2c3)));
        assertEquals(2, lanes.size());
    }

    /**
     * Creates a new point coding
     * @param startMs startMs of the point coding
     * @return a new Coding instance
     */
    private Coding pointCoding(int startMs) {
        return new Coding(subject, pointAction, startMs);
    }

    /**
     * Creates a new state coding
     * @param startMs startMs of the state coding
     * @return a new Coding instance
     */
    private Coding stateCoding(int startMs) {
        return new Coding(subject, stateAction, startMs);
    }

    /**
     * Creates a new state coding
     * @param startMs startMs of the state coding
     * @param endMs endMs of the state coding
     * @return a new Coding instance
     */
    private Coding stateCoding(int startMs, int endMs) {
        return new Coding(subject, stateAction, startMs, endMs);
    }
}
