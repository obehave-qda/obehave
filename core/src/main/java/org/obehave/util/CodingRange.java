package org.obehave.util;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import org.obehave.model.Coding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@code CodingRange} is able to calculate how many codings a particular coding overlaps.
 */
public class CodingRange {
    private Map<Coding, Range<Long>> ranges = new HashMap<>();

    /**
     * Adds a coding to the CodingRange. Updating a current coding is also possible, ie. when a coding is finished.
     *
     * @param coding the coding to add or update
     */
    public void addOrUpdate(Coding coding) {
        ranges.put(coding, getRangeForCoding(coding));
    }

    /**
     * Returns all codings {@code coding} overlaps at the specified time.
     * A {@code coding} will overlap another, existing coding only when it has already started, meaning
     * {@code currentTime >= coding.getStartMs()}
     * <p/>
     * A coding can't overlap itself.
     *
     * @param coding      the coding for which the overlaps should be counted
     * @param currentTime the time to take into account when comparing with an open coding
     * @return a list of overlapped codings
     */
    public List<Coding> overlappingCodings(Coding coding, long currentTime) {
        Range<Long> codingRange = getRangeForCoding(coding);
        List<Coding> overlaps = new ArrayList<>();

        for (Map.Entry<Coding, Range<Long>> entry : ranges.entrySet()) {
            if (!entry.getKey().equals(coding)) {
                Range<Long> range = entry.getValue();

                // if there is no upper bound, we have to take the current time into account
                if ((range.hasUpperBound() || currentTime >= coding.getStartMs()) && overlapping(range, codingRange)) {
                    overlaps.add(entry.getKey());
                }
            }
        }

        return overlaps;
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
    public int overlapCount(Coding coding, long currentTime) {
        return overlappingCodings(coding, currentTime).size();
    }

    /**
     * Checks if {@code r2} overlaps {@code r1}.
     */
    private boolean overlapping(Range<Long> r1, Range<Long> r2) {
        // singletons overlap
        if (isSingleton(r1) || isSingleton(r2)) {
            return r1.isConnected(r2);
        } else {
            return r1.isConnected(r2) && !(r1.hasUpperBound() && r1.upperEndpoint().equals(r2.lowerEndpoint()));
        }

    }

    private boolean isSingleton(Range<?> range) {
        return range.hasLowerBound() && range.hasUpperBound()
                && range.lowerBoundType() == BoundType.CLOSED && range.upperBoundType() == BoundType.CLOSED
                && range.lowerEndpoint().equals(range.upperEndpoint());
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
     * Removes all stored codings from this CodingRange.
     */
    public void clear() {
        ranges.clear();
    }

    private Range<Long> getRangeForCoding(Coding coding) {
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
}
