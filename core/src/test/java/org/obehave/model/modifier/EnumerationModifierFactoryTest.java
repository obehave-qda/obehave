package org.obehave.model.modifier;

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

    private ModifierFactory factory;

    @Before
    public void prepare() {
        factory = new ModifierFactory(VALID_STRINGS);
    }

    @Test
    public void constructionWorksWithNothing() {
        assertTrue(new ModifierFactory((String[]) null).getValidValues().isEmpty());
    }

    @Test
    public void constructionWorksWithValidStringsGiven() {
        assertArrayEquals(factory.getValidValues().toArray(), VALID_STRINGS);
    }

    @Test
    public void addAllWorks() {
        ModifierFactory factory = new ModifierFactory((String[]) null);
        factory.addValidValues(VALID_STRINGS);

        assertEquals(factory.getValidValues(), new ArrayList<>(Arrays.asList(VALID_STRINGS)));
    }

    @Test
    public void creationOfModifierWithValidString() throws FactoryException {
        Modifier modifier = factory.create("WEST");

        assertEquals(modifier.get(), "WEST");
    }

    @Test(expected = FactoryException.class)
    public void noCreationOfModifierWithoutValidString() throws FactoryException {
        factory.create("Penguin");
    }

    @Test
    public void factoryNameAndDisplayString() {
        factory.setName("Random name");
        assertEquals(factory.getName(), "Random name");
        assertEquals(factory.getDisplayString(), "Random name");
    }

    @Test
    public void factoryAlias() {
        factory.setAlias("Random alias");
        assertEquals(factory.getAlias(), "Random alias");
    }
}
