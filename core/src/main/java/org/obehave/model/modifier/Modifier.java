package org.obehave.model.modifier;

/**
 * A modifier can modify a coded action with other classes of type M
 * @param <M> the type of the modifying classes
 */
public abstract class Modifier<M> {
    /**
     * Returns the modifying class
     * @return the modifying class
     */
    public abstract M get();
}
