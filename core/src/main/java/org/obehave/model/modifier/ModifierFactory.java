package org.obehave.model.modifier;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.obehave.exceptions.FactoryException;
import org.obehave.model.Displayable;

/**
 * A class to create valid modifiers
 */
@DatabaseTable(tableName = "ModifierFactory")
public abstract class ModifierFactory<T extends Modifier> implements Displayable {
    @DatabaseField(columnName = "type")
    private Class<? extends ModifierFactory> type;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "alias")
    private String alias;

    public ModifierFactory(Class<? extends ModifierFactory> type) {
        this.type = type;
    }

    public abstract T create(String input) throws FactoryException;

    /**
     * Gets the name of the modifier factory
     * @return a name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the modifier factory
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the alias for autocompletion
     * @return a alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the alias for autocompletion
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String getDisplayString() {
        return getName();
    }
}
