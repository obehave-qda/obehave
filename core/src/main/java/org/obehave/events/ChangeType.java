package org.obehave.events;

public enum ChangeType {
    /**
     * When a new object is created
     */
    CREATE,
    /**
     * When a property of an object is updated
     */
    UPDATE,
    /**
     * When an object is deleted
     */
    DELETE;
}
