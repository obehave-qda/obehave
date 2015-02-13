package org.obehave.model.modifier;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.obehave.persistence.impl.ModifierDaoImpl;

import java.math.BigDecimal;

/**
 * @author Markus Möslinger
 */
@DatabaseTable(tableName = Modifier.ORM_TABLE, daoClass = ModifierDaoImpl.class)
public class DecimalRangeModifier extends Modifier<BigDecimal> {
    @DatabaseField(columnName = "number")
    private final BigDecimal value;

    DecimalRangeModifier(BigDecimal value) {
        super(DecimalRangeModifier.class);

        if (value == null) {
            throw new IllegalArgumentException("value must not be null!");
        }

        this.value = value;
    }

    public BigDecimal get() {
        return value;
    }
}
