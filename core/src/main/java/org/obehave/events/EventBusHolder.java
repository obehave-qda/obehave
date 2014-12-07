package org.obehave.events;

import com.google.common.eventbus.EventBus;

/**
 * A singleton implementation to provide an instance of guava's EventBus
 */
public class EventBusHolder {
    private static final EventBus INSTANCE = new EventBus();

    /**
     * Provides access to the EventBus singleton
     * @return the EventBus singleton
     */
    public static EventBus getEventBus() {
        return INSTANCE;
    }
}
