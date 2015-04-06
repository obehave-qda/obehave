package org.obehave.util;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import org.obehave.model.Coding;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@code CodingRange} is able to calculate how many codings a particular coding overlaps.
 */
class CodingRange {
    private Map<Coding, Range<Long>> ranges = new HashMap<>();

    /**
     * Adds a coding to the CodingRange. Updating a current coding is also possible, ie. when a coding is finished.
     *
     * @param coding the coding to add or update
     */
    public void add(Coding coding) {
        ranges.put(coding, getRangeForCoding(coding));
    }

    /**
     * Calculates how many codings {@code coding} overlaps at the specified time.
     * A {@code coding} will overlap another, existing coding only when it has already started, meaning
     * {@code currentTime >= coding.getStartMs()}
     * <p/>
     * A coding can't overlap itself.
     *
     * @param coding      the coding for which the overlaps should be counted
     * @param currentTime the time to take into account when comparing with an open coding
     * @return the count of overlappings at the given {@code currentTime} for coding
     */
    private int overlapCount(Coding coding, long currentTime) {
        int overlapCount = 0;
        final Range<Long> codingRange = getRangeForCoding(coding);

        for (Map.Entry<Coding, Range<Long>> entry : ranges.entrySet()) {
            final Coding forCoding = entry.getKey();

            if (!forCoding.equals(coding)) {
                Range<Long> range = entry.getValue();

                if (overlapping(range, codingRange)) {
                    if ((range.hasUpperBound() && codingRange.hasUpperBound())
                            || currentTime >= coding.getStartMs() && currentTime >= forCoding.getStartMs()) {
                        overlapCount++;
                    }
                }
            }
        }

        return overlapCount;
    }

    /**
     * Calculates how many codings {@code coding} overlaps.
     * <p/>
     * A coding can't overlap itself.
     *
     * @param coding the coding for which the overlaps should be counted
     * @return the count of overlappings at the given {@code currentTime} for coding
     */
    public int overlapCount(Coding coding) {
        return overlapCount(coding, Long.MAX_VALUE);
    }

    /**
     * Checks if two ranges overlap each other.
     */
    private static boolean overlapping(Range<Long> r1, Range<Long> r2) {
        // singletons overlap
        if (isSingleton(r1) || isSingleton(r2)) {
            return r1.isConnected(r2);
        } else {
            return r1.isConnected(r2)
                    && !(r1.hasUpperBound() && r1.upperEndpoint().equals(r2.lowerEndpoint()))
                    && !(r2.hasUpperBound() && r2.upperEndpoint().equals(r1.lowerEndpoint()));
        }

    }

    private static Range<Long> getRangeForCoding(Coding coding) {
        Range<Long> range;

        if (!coding.isStateCoding()) {
            range = Range.singleton(coding.getStartMs());
        } else {
            if (!coding.isOpen()) {
                range = Range.closedOpen(coding.getStartMs(), coding.getEndMs());
            } else {
                range = Range.atLeast(coding.getStartMs());
            }
        }

        return range;
    }

    private static boolean isSingleton(Range<?> range) {
        return range.hasLowerBound() && range.hasUpperBound()
                && range.lowerBoundType() == BoundType.CLOSED && range.upperBoundType() == BoundType.CLOSED
                && range.lowerEndpoint().equals(range.upperEndpoint());
    }
}
