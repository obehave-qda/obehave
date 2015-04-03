package org.obehave.util;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.obehave.model.Displayable;

/**
 * A wrapper class for all objects. It's only purpose is to return
 * {@link org.obehave.model.Displayable#getDisplayString()} for instances of {@code Displayable}.
 *
 * If something isn't displayable, {@code toString} is going to be called.
 */
public class DisplayWrapper<T> {
    private final T object;

    private DisplayWrapper(T object) {
        this.object = object;
    }

    /**
     * Creates a new {@code DisplayWrapper} for the given object
     * @param object the object to wrap
     * @return a new {@code DisplayWrapper} instance, wrapping {@code object}
     */
    public static <T> DisplayWrapper<T> of(T object) {
        return new DisplayWrapper<>(object);
    }

    /**
     * Returns the wrapped object
     * @return the wrapped object
     */
    public T get() {
        return object;
    }

    @Override
    public String toString() {
        if (object == null) {
            return "";
        } else if (object instanceof Displayable) {
            return ((Displayable) object).getDisplayString();
        } else {
            return object.toString();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        DisplayWrapper rhs = (DisplayWrapper) obj;

        return new EqualsBuilder().append(object, rhs.object).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(object).build();
    }
}
