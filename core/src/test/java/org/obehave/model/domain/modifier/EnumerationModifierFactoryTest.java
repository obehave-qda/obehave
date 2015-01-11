package org.obehave.model.domain.modifier;

import org.junit.Before;
import org.junit.Test;
import org.obehave.exceptions.FactoryException;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Markus Möslinger
 */
public class EnumerationModifierFactoryTest {
    private static final String[] VALID_STRINGS = new String[]{"NORTH", "EAST", "WEST", "SOUTH"};

    private EnumerationModifierFactory factory;

    @Before
    public void prepare() {
        factory = new EnumerationModifierFactory(VALID_STRINGS);
    }

    @Test
    public void constructionWorksWithNothing() {
        assertTrue(new EnumerationModifierFactory().getValidValues().isEmpty());
    }

    @Test
    public void constructionWorksWithValidStringsGiven() {
        assertArrayEquals(factory.getValidValues().toArray(), VALID_STRINGS);
    }

    @Test
    public void addAllWorks() {
        EnumerationModifierFactory factory = new EnumerationModifierFactory();
        factory.addValidValues(VALID_STRINGS);

        assertEquals(factory.getValidValues(), new ArrayList<>(Arrays.asList(VALID_STRINGS)));
    }

    @Test
    public void creationOfModifierWithValidString() throws FactoryException {
        EnumerationModifier modifier = factory.create("WEST");

        assertEquals(modifier.get(), "WEST");
    }

    @Test(expected = FactoryException.class)
    public void noCreationOfModifierWithoutValidString() throws FactoryException {
        factory.create("Penguin");
    }
}
