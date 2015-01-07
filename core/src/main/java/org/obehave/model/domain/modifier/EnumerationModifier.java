package org.obehave.model.domain.modifier;

/**
 * @author Markus Möslinger
 */
public class EnumerationModifier extends Modifier<String> {
    private final String value;

    EnumerationModifier(String value) {
        this.value = value;
    }

    @Override
    public String get() {
        return value;
    }
}
