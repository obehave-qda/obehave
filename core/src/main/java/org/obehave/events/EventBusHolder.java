package org.obehave.events;

import com.google.common.eventbus.EventBus;
import org.obehave.util.I18n;

/**
 * Holds a singleton instance of an EventBus
 */
public class EventBusHolder {
    private static final EventBus INSTANCE = new EventBus();

    private EventBusHolder() {
        throw new AssertionError(I18n.getString("exception.constructor.utility"));
    }

    /**
     * Provides access to the EventBus singleton
     * @return the EventBus singleton
     */
    private static EventBus getEventBus() {
        return INSTANCE;
    }

    /**
     * @see com.google.common.eventbus.EventBus#post(Object)
     */
    public static void post(Object event) {
        getEventBus().post(event);
    }

    /**
     * @see com.google.common.eventbus.EventBus#register(Object)
     */
    public static void register(Object object) {
        getEventBus().register(object);
    }

    /**
     * @see com.google.common.eventbus.EventBus#unregister(Object)
     */
    public static void unregister(Object object) {
        getEventBus().unregister(object);
    }
}
