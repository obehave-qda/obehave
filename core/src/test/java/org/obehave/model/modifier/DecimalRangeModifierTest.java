package org.obehave.model.modifier;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * @author Markus Möslinger
 */
public class DecimalRangeModifierTest {
    private Modifier modifier;
    private ModifierFactory modifierFactory;

    @Before
    public void prepare() {
        modifierFactory = new ModifierFactory(0, 10);
        modifier = new Modifier(modifierFactory, BigDecimal.valueOf(5.5), "5.5");
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotCreateModifierWithNull() {
        new Modifier(modifierFactory, (BigDecimal) null, null);
    }

    @Test
    public void modifierReturnsWrappedValue() {
        assertEquals(modifier.get(), BigDecimal.valueOf(5.5));
    }
}
