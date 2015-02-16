package org.obehave.persistence.ormlite;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * This class is mostly copied from {@link com.j256.ormlite.field.types.BaseDateType} and {@link com.j256.ormlite.field.types.DateType},
 * to get a persister for {@link org.joda.time.DateTime} that supports versioning
 * <p />
 * This is fixed in ormlite 4.49
 *
 * @see <a href="https://github.com/j256/ormlite-core/issues/47">https://github.com/j256/ormlite-core/issues/47</a>
 */
public class VersionDateTimeType extends BaseDataType {
    private static final Logger log = LoggerFactory.getLogger(VersionDateTimeType.class);

    private static final VersionDateTimeType INSTANCE = new VersionDateTimeType();

    private VersionDateTimeType() {
        super(SqlType.DATE, new Class[]{DateTime.class});
    }

    protected VersionDateTimeType(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }

    public static VersionDateTimeType getInstance() {
        return INSTANCE;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        if (javaObject == null) {
            return null;
        } else {
            return new Timestamp(((DateTime) javaObject).getMillis());
        }
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        log.info("Doing whatever with default string {} and field type {}", defaultStr, fieldType);
        return new Timestamp(Long.parseLong(defaultStr));
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getTimestamp(columnPos);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        Timestamp value = (Timestamp) sqlArg;
        return new DateTime(value.getTime());
    }

    @Override
    public boolean isEscapedValue() {
        return false;
    }

    @Override
    public boolean isAppropriateId() {
        return false;
    }

    @Override
    public boolean isValidForVersion() {
        return true;
    }

    @Override
    public Object moveToNextValue(Object currentValue) {
        DateTime now = new DateTime();
        if (currentValue == null) {
            return now;
        } else if (now.equals(currentValue)) {
            return now.plusMillis(1);
        } else {
            return now;
        }
    }
}
