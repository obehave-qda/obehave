package org.obehave.util;

import com.google.common.collect.TreeMultiset;
import org.obehave.model.Coding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A {@code CodingArranger} is able to arrange parallel codings in multiple lanes.
 */
public class CodingArranger {
    private TreeMultiset<Coding> codings = TreeMultiset.create(new CodingStartTimeComparator());
    private List<List<Coding>> lanes = new ArrayList<>();

    /**
     * Creates a new {@code CodingArranger}
     */
    public CodingArranger() {
        lanes.add(new ArrayList<Coding>());
    }

    /**
     * Adds a coding to the arranger and returns the corresponding lane, or -1 if a new one had to be created
     * <p/>
     * If {@code -1} was returned, a {@link CodingArranger#readjust()} could be necessary. To obtain
     * the lane of the new coding, when a new lane had to be created, you can call {@link CodingArranger#getLaneCount()}
     *
     * @param coding the coding to add
     * @return the lane in which the coding was put, or -1 if a new lane had to be created
     */
    public int add(Coding coding) {
        codings.add(coding);

        return addCodingToFreeLane(coding);
    }

    /**
     * Returns the number of lanes needed for arrangement
     * @return the number of lanes
     */
    public int getLaneCount() {
        return lanes.size();
    }

    /**
     * Recalculates everything again, sorting lanes in a deterministic way.
     * @return the readjusted lanes
     */
    public List<List<Coding>> readjust() {
        lanes.clear();
        lanes.add(new ArrayList<Coding>());

        for (Coding coding : codings) {
            addCodingToFreeLane(coding);
        }

        return lanes;
    }

    private int addCodingToFreeLane(Coding coding) {
        int lane = getLaneWithFreePosition(coding);

        if (lane >= 0) {
            lanes.get(lane).add(coding);
        } else {
            final ArrayList<Coding> newLane = new ArrayList<>();
            newLane.add(coding);
            lanes.add(newLane);
        }

        return lane;
    }

    private int getLaneWithFreePosition(Coding coding) {
        int currentLane = 0;

        for (List<Coding> lane : lanes) {
            CodingRange range = new CodingRange();

            // add all codings of lane to range
            for (Coding c : lane) {
                range.add(c);
            }

            // would there be any overlaps?
            if (range.overlapCount(coding) == 0) {
                return currentLane;
            }

            currentLane++;
        }


        return -1;
    }

    private static class CodingStartTimeComparator implements Comparator<Coding> {
        @Override
        public int compare(Coding o1, Coding o2) {
            return (int) (o1.getStartMs() - o2.getStartMs());
        }
    }
}
