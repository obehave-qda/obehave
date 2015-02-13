package org.obehave.model.modifier;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.obehave.persistence.impl.ModifierDaoImpl;

/**
 * A modifier can modify a coded action with other classes of type M
 * @param <M> the type of the modifying classes
 */
public abstract class Modifier<M> {
    public static final String ORM_TABLE = "Modifier";

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
