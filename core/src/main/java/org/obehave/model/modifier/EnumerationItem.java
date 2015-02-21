package org.obehave.model.modifier;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.obehave.model.BaseEntity;

/**
 * @author Markus Möslinger
 */
@DatabaseTable(tableName = "EnumerationItem")
public class EnumerationItem extends BaseEntity {
    @DatabaseField(columnName = "value")
    private String value;

    @DatabaseField(columnName = "modifierFactory", foreign = true)
    private ModifierFactory modifierFactory;

    private EnumerationItem() {
        // for frameworks
    }

    public EnumerationItem(String value, ModifierFactory modifierFactory) {
        this.value = value;
        this.modifierFactory = modifierFactory;
    }

    public String getValue() {
        return value;
    }
}
