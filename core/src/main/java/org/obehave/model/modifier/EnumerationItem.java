package org.obehave.model.modifier;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.obehave.model.BaseEntity;
import org.obehave.persistence.impl.EnumerationItemDaoImpl;

/**
 * @author Markus Möslinger
 */
@DatabaseTable(tableName = "EnumerationItem", daoClass = EnumerationItemDaoImpl.class)
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("modifierFactory", modifierFactory).append("value", value).toString();
    }
}
