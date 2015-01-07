package org.obehave.model.domain.modifier;

import org.obehave.exceptions.FactoryException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

/**
 * @author Markus Möslinger
 */
public class DecimalRangeModifierFactory extends ModifierFactory<DecimalRangeModifier> {
    private int from;
    private int to;

    @Override
    public DecimalRangeModifier create(String input) throws FactoryException {
        BigDecimal value = null;
        try {
            value = stringToBigDecimal(input);
            if (value.compareTo(BigDecimal.valueOf(from)) >= 0 && value.compareTo(BigDecimal.valueOf(to)) <= 0) {
                return new DecimalRangeModifier(value);
            } else {
                throw new FactoryException("Value not in range");
            }
        } catch (ParseException e) {
            throw new FactoryException("Couldn't create instance with this input", e);
        }
    }

    private static BigDecimal stringToBigDecimal(String input) throws ParseException {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.getDefault());
        String pattern = "#,##0.0#";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);

        return (BigDecimal) decimalFormat.parse("10,692,467,440,017.120");
    }
}
