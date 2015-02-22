package org.obehave.model.modifier;

import org.junit.Before;
import org.junit.Test;
import org.obehave.exceptions.FactoryException;
import org.obehave.exceptions.ValidationException;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * @author Markus Möslinger
 */
public class DecimalRangeModifierFactoryTest {
    private ModifierFactory factory;

    @Before
    public void prepare() {
        factory = new ModifierFactory(-15, 15);
    }

    @Test
    public void constructorSwapsDecimalsIfNeeded() {
        ModifierFactory factory = new ModifierFactory(10, 5);

        assertEquals(5, factory.getFrom());
        assertEquals(10, factory.getTo());
    }

    @Test
    public void constructorCreatesObjectsWithSameFromAndTo() {
        ModifierFactory factory = new ModifierFactory(10, 10);

        assertEquals(10, factory.getFrom());
        assertEquals(10, factory.getTo());
    }

    public void setRangeWorks() {
        factory.setRange(-5, 5);

        assertEquals(-5, factory.getFrom());
        assertEquals(5, factory.getTo());
    }

    public void setRangeWorksWithSwappingOfFromAndTo() {
        factory.setRange(10, 5);

        assertEquals(5, factory.getFrom());
        assertEquals(10, factory.getTo());
    }

    public void setRangeWorksWithSameFromAndTo() {
        factory.setRange(-10, -10);

        assertEquals(-10, factory.getFrom());
        assertEquals(-10, factory.getTo());
    }

    @Test(expected = ValidationException.class)
    public void cannotCreateModifierWithNullInput() throws FactoryException {
        factory.create(null);
    }

    @Test(expected = ValidationException.class)
    public void cannotCreateModifierWithEmptyString() throws FactoryException {
        factory.create("");
    }

    @Test
    public void createModifierWithInteger() throws FactoryException {
        Modifier modifier = factory.create("5");

        assertEquals(BigDecimal.valueOf(5), modifier.get());
    }

    @Test
    public void createModifierWithFloatDotAndGermanLocale() throws FactoryException {
        checkCreationWithLocale(5.5, "5.5", Locale.GERMANY);
    }

    @Test
    public void createModifierWithFloatDotAndEnglishLocale() throws FactoryException {
        checkCreationWithLocale(5.5, "5.5", Locale.US);
    }

    @Test(expected = FactoryException.class)
    public void cannotCreateModifierWithFloatCommaAndGermanLocale() throws FactoryException {
        checkCreationWithLocale(5.5, "5,5", Locale.GERMANY);
    }

    @Test(expected = FactoryException.class)
    public void cannotCreateModifierWithFloatCommaAndEnglishLocale() throws FactoryException {
        checkCreationWithLocale(5.5, "5,5", Locale.US);
    }

    private void checkCreationWithLocale(double expected, String input, Locale locale) throws FactoryException {
        Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(locale);

        try {
            Modifier modifier = factory.create(input);

            assertEquals(modifier.get(), BigDecimal.valueOf(expected));
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }

    @Test
    public void createModifierWithNegativeNumber() throws FactoryException {
        Modifier modifier = factory.create("-5");

        assertEquals(BigDecimal.valueOf(-5), modifier.get());
    }

    @Test(expected = FactoryException.class)
    public void cannotCreateModifierWithStringInsteadOfNumber() throws FactoryException {
        factory.create("not a number");
    }

    @Test
    public void factoryNameAndDisplayString() {
        factory.setName("Random name");
        assertEquals("Random name", factory.getName());
        assertEquals("Random name", factory.getDisplayString());
    }

    @Test
    public void factoryAlias() {
        factory.setAlias("Random alias");
        assertEquals("Random alias", factory.getAlias());
    }
}
