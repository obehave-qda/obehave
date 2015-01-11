package org.obehave.model.domain.modifier;

import java.math.BigDecimal;

/**
 * @author Markus Möslinger
 */
public class DecimalRangeModifier extends Modifier<BigDecimal> {
    private final BigDecimal value;

    DecimalRangeModifier(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null!");
        }

        this.value = value;
    }

    public BigDecimal get() {
        return value;
    }
}
