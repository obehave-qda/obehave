package org.obehave.model.domain.modifier;

import org.junit.Before;
import org.junit.Test;
import org.obehave.exceptions.FactoryException;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * @author Markus Möslinger
 */
public class DecimalRangeModifierFactoryTest {
    private DecimalRangeModifierFactory factory;

    @Before
    public void prepare() {
        factory = new DecimalRangeModifierFactory(-15, 15);
    }

    @Test
    public void constructionWorks() {
        DecimalRangeModifierFactory factory = new DecimalRangeModifierFactory();

        assertEquals(factory.getFrom(), 0);
        assertEquals(factory.getTo(), 0);

        factory = new DecimalRangeModifierFactory(5, 10);

        assertEquals(factory.getFrom(), 5);
        assertEquals(factory.getTo(), 10);
    }

    @Test
    public void constructorSwapsDecimalsIfNeeded() {
        DecimalRangeModifierFactory factory = new DecimalRangeModifierFactory(10, 5);

        assertEquals(factory.getFrom(), 5);
        assertEquals(factory.getTo(), 10);
    }

    @Test
    public void constructorCreatesObjectsWithSameFromAndTo() {
        DecimalRangeModifierFactory factory = new DecimalRangeModifierFactory(10, 10);

        assertEquals(factory.getFrom(), 10);
        assertEquals(factory.getTo(), 10);
    }

    public void setRangeWorks() {
        factory.setRange(-5, 5);

        assertEquals(factory.getFrom(), -5);
        assertEquals(factory.getTo(), 5);
    }

    public void setRangeWorksWithSwappingOfFromAndTo() {
        factory.setRange(10, 5);

        assertEquals(factory.getFrom(), 5);
        assertEquals(factory.getTo(), 10);
    }

    public void setRangeWorksWithSameFromAndTo() {
        factory.setRange(-10, -10);

        assertEquals(factory.getFrom(), -10);
        assertEquals(factory.getTo(), -10);
    }

    @Test(expected = FactoryException.class)
    public void cannotCreateModifierWithNullInput() throws FactoryException {
        factory.create(null);
    }

    @Test(expected = FactoryException.class)
    public void cannotCreateModifierWithEmptyString() throws FactoryException {
        factory.create("");
    }

    @Test
    public void createModifierWithInteger() throws FactoryException {
        DecimalRangeModifier modifier = factory.create("5");

        assertEquals(modifier.get(), BigDecimal.valueOf(5));
    }

    @Test
    public void createModifierWithFloatDotAndGermanLocale() throws FactoryException {
        checkCreationWithLocale("5.5", 5.5, Locale.GERMANY);
    }

    @Test
    public void createModifierWithFloatDotAndEnglishLocale() throws FactoryException {
        checkCreationWithLocale("5.5", 5.5, Locale.US);
    }

    @Test(expected = FactoryException.class)
    public void cannotCreateModifierWithFloatCommaAndGermanLocale() throws FactoryException {
        checkCreationWithLocale("5,5", 5.5, Locale.GERMANY);
    }

    @Test(expected = FactoryException.class)
    public void cannotCreateModifierWithFloatCommaAndEnglishLocale() throws FactoryException {
        checkCreationWithLocale("5,5", 5.5, Locale.US);
    }

    private void checkCreationWithLocale(String input, double expected, Locale locale) throws FactoryException {
        Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(locale);

        try {
            DecimalRangeModifier modifier = factory.create(input);

            assertEquals(modifier.get(), BigDecimal.valueOf(expected));
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }

    @Test
    public void createModifierWithNegativeNumber() throws FactoryException {
        DecimalRangeModifier modifier = factory.create("-5");

        assertEquals(modifier.get(), BigDecimal.valueOf(-5));
    }

    @Test(expected = FactoryException.class)
    public void cannotCreateModifierWithStringInsteadOfNumber() throws FactoryException {
        factory.create("not a number");
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
