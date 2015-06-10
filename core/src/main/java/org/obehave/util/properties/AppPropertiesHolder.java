package org.obehave.util.properties;

import org.aeonbits.owner.ConfigCache;

/**
 * @author Markus Möslinger
 */
public class AppPropertiesHolder {
    public static AppProperties get() {
        return ConfigCache.getOrCreate(AppProperties.class, System.getProperties(), System.getenv());
    }
}
