package org.obehave.util;

import org.junit.Before;
import org.junit.Test;
import org.obehave.model.Coding;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Markus Möslinger
 */
public class CodingArrangerTest extends CodingBaseTest {
    private CodingArranger codingArranger;

    @Before
    public void prepare() {
        codingArranger = new CodingArranger();
    }

    @Test
    public void arrangingOneStateAndThreePointActions() {
        Coding l1c1 = stateCoding(0, 20);
        Coding l2c1 = pointCoding(5);
        Coding l2c2 = pointCoding(10);
        Coding l2c3 = pointCoding(15);

        codingArranger.add(l2c1);
        codingArranger.add(l2c2);
        codingArranger.add(l2c3);
        codingArranger.add(l1c1);

        final List<List<Coding>> lanes = codingArranger.readjust();
        assertTrue(lanes.get(0).contains(l1c1));
        assertTrue(lanes.get(1).containsAll(Arrays.asList(l2c1, l2c2, l2c3)));
        assertEquals(2, lanes.size());
    }

    @Test
    public void arrangeFourStateCodings() {
        Coding l1c1 = stateCoding(5, 15);
        Coding l2c1 = stateCoding(7, 17);
        Coding l3c1 = stateCoding(10, 20);
        Coding l1c2 = stateCoding(17, 27);

        codingArranger.add(l1c1);
        codingArranger.add(l2c1);
        codingArranger.add(l3c1);
        codingArranger.add(l1c2);

        final List<List<Coding>> lanes = codingArranger.readjust();
        assertEquals(3, lanes.size());
        assertTrue(lanes.get(0).containsAll(Arrays.asList(l1c1, l1c2)));
        assertTrue(lanes.get(1).contains(l2c1));
        assertTrue(lanes.get(2).contains(l3c1));
    }
}
