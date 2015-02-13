package org.obehave.model.modifier;

import com.j256.ormlite.field.DatabaseField;

/**
 * @author Markus Möslinger
 */
public class EnumerationModifier extends Modifier<String> {
    @DatabaseField(columnName = "enumerationValue")
    private final String value;

    EnumerationModifier(String value) {
        super(EnumerationModifier.class);

        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }

        this.value = value;
    }

    @Override
    public String get() {
        return value;
    }
}
