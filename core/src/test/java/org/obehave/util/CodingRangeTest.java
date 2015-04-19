package org.obehave.util;

import org.junit.Before;
import org.junit.Test;
import org.obehave.model.Coding;
import org.obehave.model.Subject;

import static org.junit.Assert.assertEquals;

public class CodingRangeTest extends CodingBaseTest {
    private CodingRange codingRange;

    @Before
    public void prepare() {
        codingRange = new CodingRange();
    }

    @Test
    public void pointCodingOverlapsPointCoding() {
        final Coding coding = pointCoding(5);
        coding.setSubject(new Subject("We don't want equal point codings"));
        codingRange.add(coding);

        assertEquals(1, codingRange.overlapCount(pointCoding(5)));
    }

    @Test
    public void pointCodingOverlapsStateCoding() {
        codingRange.add(stateCoding(0, 10));

        assertEquals(1, codingRange.overlapCount(pointCoding(5)));
    }

    @Test
    public void stateCodingOverlapsPointCoding() {
        codingRange.add(pointCoding(5));

        assertEquals(1, codingRange.overlapCount(stateCoding(0, 10)));
    }

    @Test
    public void stateCodingOverlapsStateCoding() {
        codingRange.add(stateCoding(0, 10));

        assertEquals(1, codingRange.overlapCount(stateCoding(5, 15)));
    }

    @Test
    public void stateCodingOverlapsOpenStateCoding() {
        codingRange.add(stateCoding(5));

        assertEquals(1, codingRange.overlapCount(stateCoding(7, 15)));
    }

    @Test
    public void pointCodingOverlapsOpenStateCoding() {
        codingRange.add(stateCoding(5));

        assertEquals(1, codingRange.overlapCount(pointCoding(7)));
    }

    @Test
    public void pointCodingDoesntOverlapPointCoding() {
        codingRange.add(pointCoding(5));

        assertEquals(0, codingRange.overlapCount(pointCoding(10)));
    }

    @Test
    public void pointCodingDoesntOverlapStateCoding() {
        codingRange.add(stateCoding(0, 5));

        assertEquals(0, codingRange.overlapCount(pointCoding(10)));
    }

    @Test
    public void stateCodingDoesntOverlapPointCoding() {
        codingRange.add(pointCoding(5));

        assertEquals(0, codingRange.overlapCount(stateCoding(10, 15)));
    }

    @Test
    public void stateCodingDoesntOverlapStateCoding() {
        codingRange.add(stateCoding(0, 5));

        assertEquals(0, codingRange.overlapCount(stateCoding(10, 15)));
    }

    @Test
    public void pointCodingOverlapsTwoCodings() {
        codingRange.add(pointCoding(0)); // no overlapping
        codingRange.add(stateCoding(0, 10)); // overlapping
        codingRange.add(stateCoding(3, 11)); // overlapping

        assertEquals(2, codingRange.overlapCount(pointCoding(5)));
    }

    @Test
    public void stateCodingOverlapsTwoCodings() {
        codingRange.add(pointCoding(0)); // no overlapping
        codingRange.add(stateCoding(0, 10)); // overlapping
        codingRange.add(stateCoding(3, 11)); // overlapping

        assertEquals(2, codingRange.overlapCount(stateCoding(4, 15)));
    }

    @Test
    public void pointCodingOverlapsNowClosedStateCoding() {
        final Coding coding = stateCoding(0);
        codingRange.add(coding);

        assertEquals(0, codingRange.overlapCount(pointCoding(5)));

        coding.setEndMs(10);
        assertEquals(0, codingRange.overlapCount(pointCoding(5)));
        codingRange.add(coding);

        assertEquals(1, codingRange.overlapCount(pointCoding(5)));
    }

    @Test
    public void codingDoesntOverlapItself() {
        final Coding coding = pointCoding(5);

        codingRange.add(coding);

        assertEquals(0, codingRange.overlapCount(coding));
    }

    @Test
    public void adjacentStateCodingsDontOverlap() {
        codingRange.add(stateCoding(0, 10));

        assertEquals(0, codingRange.overlapCount(stateCoding(10, 20)));
    }

    @Test
    public void adjacentPointCodingsDontOverlap() {
        codingRange.add(pointCoding(5));

        assertEquals(0, codingRange.overlapCount(pointCoding(6)));
    }
}
