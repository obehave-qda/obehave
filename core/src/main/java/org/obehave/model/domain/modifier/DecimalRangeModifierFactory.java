package org.obehave.model.domain.modifier;

import org.obehave.exceptions.FactoryException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * @author Markus Möslinger
 */
public class DecimalRangeModifierFactory extends ModifierFactory<DecimalRangeModifier> {
    private int from;
    private int to;

    public DecimalRangeModifierFactory() {

    }

    public DecimalRangeModifierFactory(int from, int to) {
        setRange(from, to);
    }

    public void setRange(int from, int to) {
        if (from > to) {
            int tmp = to;
            to = from;
            from = tmp;
        }

        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    @Override
    public DecimalRangeModifier create(String input) throws FactoryException {
        if (input == null || input.equals("")) {
            throw new FactoryException("input must be a non empty string");
        }

        BigDecimal value;
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
        return stringToBigDecimal(input, Locale.US);
    }

    private static BigDecimal stringToBigDecimal(String input, Locale locale) throws ParseException {
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(locale);
        decimalFormat.setParseBigDecimal(true);

        return (BigDecimal) decimalFormat.parse(input);
    }
}
