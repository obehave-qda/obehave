package org.obehave.model.modifier;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Markus Möslinger
 */
public class EnumerationModifierTest {
    private Modifier modifier;

    @Before
    public void prepare() {
        modifier = new Modifier("NORTH");
    }

    @Test(expected = IllegalArgumentException.class)
    public void creationWithNullFails() {
        new Modifier((String) null);
    }

    @Test
    public void creationWithEmptyString() {
        assertEquals("", new Modifier("").get());
    }

    @Test
    public void getWorks() {
        assertEquals("NORTH", modifier.get());
    }
}
