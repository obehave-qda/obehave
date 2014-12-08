package org.obehave.events;

public class ChangeEvent<T> {
    private final T changedObject;
    private final ChangeType changeType;
    private final Class<?> changeClass;

    public ChangeEvent(T changedObject, ChangeType changeType) {
        this.changedObject = changedObject;
        this.changeType = changeType;
        changeClass = changedObject.getClass();
    }

    public T getChanged() {
        return changedObject;
    }

    public ChangeType getChangeType() {
        return changeType;
    }
}
