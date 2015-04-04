package org.obehave.view.util;

import javafx.application.Application;
import javafx.application.HostServices;

/**
 * Since {@link HostServices} are only available when subclassing {@link javafx.application.Application},
 * this class can be initialized with the value of {@link Application#getHostServices()} to access {@code HostServices}
 * from everywhere.
 */
public class HostServicesHolder {
    private static HostServices hostServices;

    public static void initialize(HostServices hostServices) {
        if (HostServicesHolder.hostServices != null) {
            throw new IllegalStateException("HostServicesHolder already initialized");
        }

        HostServicesHolder.hostServices = hostServices;
    }

    public static HostServices get() {
        if (hostServices == null) {
            throw new IllegalStateException("HostServicesHolder not yet initialized");
        }

        return hostServices;
    }
}
