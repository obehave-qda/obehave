package org.obehave.model.modifier;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * A modifier can modify a coded action with other classes of type M
 * @param <M> the type of the modifying classes
 */
@DatabaseTable(tableName = "Modifier")
public abstract class Modifier<M> {
    @DatabaseField(columnName = "type")
    private Class<? extends Modifier> type;

    public Modifier(Class<? extends Modifier> type) {
        this.type = type;
    }

    /**
     * Returns the modifying class
     * @return the modifying class
     */
    public abstract M get();
}
