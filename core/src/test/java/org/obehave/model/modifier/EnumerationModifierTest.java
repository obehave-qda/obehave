package org.obehave.model.modifier;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Markus Möslinger
 */
public class EnumerationModifierTest {
    private ModifierFactory modifierFactory;
    private Modifier modifier;

    @Before
    public void prepare() {
        modifierFactory = new ModifierFactory("NORTH");
        modifier = new Modifier(modifierFactory, "NORTH");
    }

    @Test(expected = IllegalArgumentException.class)
    public void creationWithNullFails() {
        new Modifier(modifierFactory, (String) null);
    }

    @Test
    public void creationWithEmptyString() {
        assertEquals("", new Modifier(modifierFactory, "").get());
    }

    @Test
    public void getWorks() {
        assertEquals("NORTH", modifier.get());
    }
}
