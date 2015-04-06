package org.obehave.util;

import com.google.common.collect.TreeMultiset;
import org.obehave.model.Coding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A {@code CodingRange} is able to calculate how many codings a particular coding overlaps.
 */
public class CodingArranger {
    private TreeMultiset<Coding> codings = TreeMultiset.create(new CodingStartTimeComparator());
    private List<List<Coding>> lanes = new ArrayList<>();

    public CodingArranger() {
        lanes.add(new ArrayList<Coding>());
    }

    /**
     * Adds a coding to the arranger and returns the corresponding lane, or -1 if a new one had to be created
     *
     * @param coding the coding to add
     * @return the lane in which the coding was put, or -1 if a new lane had to be created
     */
    public int add(Coding coding) {
        codings.add(coding);

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

    public int getLaneForCoding(Coding coding) {
        for (int i = 0; i < lanes.size(); i++) {
            if (lanes.get(i).contains(coding)) {
                return i;
            }
        }

        throw new IllegalArgumentException("Coding couldn't be found in CodingArranger - you have to add it first");
    }

    public int getLaneCount() {
        return lanes.size();
    }

    /**
     * Returns the current coding arrangement
     * @return the current coding arrangement
     */
    public List<List<Coding>> getCodingArrangement() {
        return lanes;
    }

    private int getLaneWithFreePosition(Coding coding) {
        int currentLane = 0;

        for (List<Coding> lane : lanes) {
            CodingRange range = new CodingRange();

            // add all codings of lane to range
            for (Coding c : lane) {
                range.addOrUpdate(c);
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
