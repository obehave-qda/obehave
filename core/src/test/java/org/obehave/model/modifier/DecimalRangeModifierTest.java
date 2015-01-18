package org.obehave.model.modifier;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * @author Markus Möslinger
 */
public class DecimalRangeModifierTest {
    private DecimalRangeModifier modifier;

    @Before
    public void prepare() {
        modifier = new DecimalRangeModifier(BigDecimal.valueOf(5.5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotCreateModifierWithNull() {
        new DecimalRangeModifier(null);
    }

    @Test
    public void modifierReturnsWrappedValue() {
        assertEquals(modifier.get(), BigDecimal.valueOf(5.5));
    }
}
