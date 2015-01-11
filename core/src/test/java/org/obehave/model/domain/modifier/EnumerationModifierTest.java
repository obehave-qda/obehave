package org.obehave.model.domain.modifier;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Markus Möslinger
 */
public class EnumerationModifierTest {
    private EnumerationModifier modifier;

    @Before
    public void prepare() {
        modifier = new EnumerationModifier("NORTH");
    }

    @Test(expected = IllegalArgumentException.class)
    public void creationWithNullFails() {
        new EnumerationModifier(null);
    }

    @Test
    public void creationWithEmptyString() {
        assertEquals(new EnumerationModifier("").get(), "");
    }

    @Test
    public void getWorks() {
        assertEquals(modifier.get(), "NORTH");
    }
}
