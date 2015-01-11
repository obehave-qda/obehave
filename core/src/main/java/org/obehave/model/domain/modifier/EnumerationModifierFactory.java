package org.obehave.model.domain.modifier;

import org.obehave.exceptions.FactoryException;

import java.util.*;

/**
 * @author Markus Möslinger
 */
public class EnumerationModifierFactory extends ModifierFactory<EnumerationModifier> {
    private List<String> validValues = new ArrayList<>();

    public EnumerationModifierFactory() {

    }

    public EnumerationModifierFactory(String... values) {
        addValidValues(values);
    }

    public boolean addValidValues(String... values) {
        return validValues.addAll(Arrays.asList(values));
    }

    @Override
    public EnumerationModifier create(String input) throws FactoryException {
        if (validValues.contains(input)) {
            return new EnumerationModifier(input);
        } else {
            throw new FactoryException("This isn't an allowed value");
        }
    }

    public List<String> getValidValues() {
        return Collections.unmodifiableList(validValues);
    }
}
