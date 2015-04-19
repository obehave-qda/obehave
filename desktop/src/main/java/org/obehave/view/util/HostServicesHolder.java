package org.obehave.view.util;

import javafx.application.Application;
import javafx.application.HostServices;
import org.obehave.util.I18n;

/**
 * Since {@link HostServices} are only available when subclassing {@link javafx.application.Application},
 * this class can be initialized with the value of {@link Application#getHostServices()} to access {@code HostServices}
 * from everywhere.
 */
public class HostServicesHolder {
    private static HostServices hostServices;

    private HostServicesHolder() {
        throw new AssertionError(I18n.get("exception.constructor.utility"));
    }

    /**
     * Initializes the {@code HostServicesHolder} with an instance of {@link HostServices}
     * @param hostServices an instance of {@code HostServices}
     * @throws IllegalStateException if the holder was already initialized
     */
    public static void initialize(HostServices hostServices) {
        if (HostServicesHolder.hostServices != null) {
            throw new IllegalStateException("HostServicesHolder already initialized");
        }

        HostServicesHolder.hostServices = hostServices;
    }

    /**
     * Returns the holded {@link HostServices}
     * @return
     * @throws IllegalStateException if {@code HostServiceHolder} wasn't initialized yet
     */
    public static HostServices get() {
        if (hostServices == null) {
            throw new IllegalStateException("HostServicesHolder not yet initialized");
        }

        return hostServices;
    }
}
