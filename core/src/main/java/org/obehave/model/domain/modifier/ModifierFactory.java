package org.obehave.model.domain.modifier;

import org.obehave.exceptions.FactoryException;

/**
 * @author Markus Möslinger
 */
public abstract class ModifierFactory<T extends Modifier> {
    private String name;
    private String alias;

    public abstract T create(String input) throws FactoryException;
}
