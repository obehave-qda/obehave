package org.obehave.util.properties;

import org.aeonbits.owner.Mutable;

import static org.aeonbits.owner.Config.Sources;

/**
 * @author Markus Möslinger
 */
@Sources({"file:~/obehave/obehave.properties", "classpath:obehave.properties"})
public interface AppProperties extends Mutable {
    @Key("language")
    String language();

    @Key("ui.error.exceptions.show")
    boolean showExceptions();

    @Key("database.suffix")
    String databaseFileSuffix();

    @Key("database.connectionstring")
    String databaseConnectionString(String file);

    @Key("database.connectionstring.initsuffix")
    String databaseConnectionInitString(String file);

    @Key("database.debug")
    boolean isDatabaseDebug();

    @Key("database.debug.port.web")
    String databaseDebugPortWeb();

    @Key("database.debug.port.tcp")
    String databaseDebugPortTcp();

    @Key("savefolder")
    String saveFolder();
}
