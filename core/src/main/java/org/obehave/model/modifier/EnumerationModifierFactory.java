package org.obehave.model.modifier;

import com.j256.ormlite.table.DatabaseTable;
import org.obehave.exceptions.FactoryException;
import org.obehave.persistence.impl.ModifierFactoryDaoImpl;

import java.util.*;

/**
 * @author Markus Möslinger
 */
@DatabaseTable(tableName = ModifierFactory.ORM_TABLE, daoClass = ModifierFactoryDaoImpl.class)
public class EnumerationModifierFactory extends ModifierFactory<EnumerationModifier> {
    private List<String> validValues = new ArrayList<>();

    public EnumerationModifierFactory() {
        super(EnumerationModifierFactory.class);
    }

    public EnumerationModifierFactory(String... values) {
        this();
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
