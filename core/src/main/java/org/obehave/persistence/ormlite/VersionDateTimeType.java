package org.obehave.persistence.ormlite;

import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.DateTimeType;

/**
 * @author Markus Möslinger
 */
public class VersionDateTimeType extends DateTimeType {
    public VersionDateTimeType(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }


}
