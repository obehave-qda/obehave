package org.obehave.events;

/**
 * @author Markus Möslinger
 */
public class AbstractEvent<T> {
    private final T object;

    public AbstractEvent(T object) {
        this.object = object;
    }

    public T get() {
        return object;
    }
}
