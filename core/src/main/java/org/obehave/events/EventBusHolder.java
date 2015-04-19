package org.obehave.events;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import org.obehave.util.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Holds a singleton instance of an EventBus
 */
public class EventBusHolder {
    private static final EventBus INSTANCE = new EventBus(Slf4JExceptionHandler.INSTANCE);

    private EventBusHolder() {
        throw new AssertionError(I18n.get("exception.constructor.utility"));
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

    static class Slf4JExceptionHandler implements SubscriberExceptionHandler {
        private static final Logger log = LoggerFactory.getLogger(EventBus.class);
        static final Slf4JExceptionHandler INSTANCE = new Slf4JExceptionHandler();

        @Override
        public void handleException(Throwable exception, SubscriberExceptionContext context) {
            log.error(message(context), exception);
        }

        private static String message(SubscriberExceptionContext context) {
            Method method = context.getSubscriberMethod();
            return "Exception thrown by subscriber method "
                    + method.getName() + '(' + method.getParameterTypes()[0].getName() + ')'
                    + " on subscriber " + context.getSubscriber()
                    + " when dispatching event: " + context.getEvent();
        }
    }
}
