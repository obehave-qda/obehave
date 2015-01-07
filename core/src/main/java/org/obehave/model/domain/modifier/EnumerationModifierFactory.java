package org.obehave.model.domain.modifier;

import org.obehave.exceptions.FactoryException;

import java.util.List;

/**
 * @author Markus Möslinger
 */
public class EnumerationModifierFactory extends ModifierFactory<EnumerationModifier> {
    private List<String> validValues;

    @Override
    public EnumerationModifier create(String input) throws FactoryException {
        if (validValues.contains(input)) {
            return new EnumerationModifier(input);
        } else {
            throw new FactoryException("This isn't an allowed value");
        }
    }
}
