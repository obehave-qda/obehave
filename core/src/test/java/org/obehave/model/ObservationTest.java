package org.obehave.model;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Markus Möslinger
 */
public class ObservationTest {
    private Observation observation;
    private static final String NAME = "Observation1";
    private static final DateTime DATE = new DateTime();
    private static final Subject subject = new Subject("Subject1");
    private static final Action action = new Action("Fighting");
    private static final Coding coding = new Coding(subject, action, 300);
    private static final Coding coding2 = new Coding(subject, action, 500);

    @Before
    public void preprare() {
        observation = new Observation(NAME);
        observation.setDateTime(DATE);

        observation.addCoding(coding);
        observation.addCoding(coding2);
    }

    @Test
    public void getWorks() {
        assertEquals(NAME, observation.getName());
        assertEquals(DATE, observation.getDateTime());
        assertTrue(observation.getCodings().contains(coding));
        assertTrue(observation.getCodings().contains(coding2));
    }
}
