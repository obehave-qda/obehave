package org.obehave.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Markus Möslinger
 */
public class SubjectTest {
    private static final String NAME = "Subject1";
    private static final String ALIAS = "Sub";
    private static final Color COLOR = Color.valueOf("#FF0000");
    private Subject subject;

    @Before
    public void prepare() {
        subject = new Subject(NAME);
        subject.setAlias(ALIAS);
        subject.setColor(COLOR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void creationWithNullAsNameDoesntWork() {
        new Subject(null);
    }

    public void gettingFieldWorkds() {
        assertEquals(NAME, subject.getName());
        assertEquals(ALIAS, subject.getAlias());
        assertEquals(COLOR, subject.getColor());
        assertEquals(NAME, subject.getDisplayString());
    }
}
