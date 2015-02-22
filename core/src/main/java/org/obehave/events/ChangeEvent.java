package org.obehave.events;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ChangeEvent<T> {
    private final T changedObject;
    private final ChangeType changeType;

    public ChangeEvent(T changedObject, ChangeType changeType) {
        this.changedObject = changedObject;
        this.changeType = changeType;
    }

    public T getChanged() {
        return changedObject;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
