package org.obehave.persistence.util;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDateType;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.support.DatabaseResults;
import org.obehave.util.DateUtil;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;

/**
 * Persister to be able to create and read LocalDateTime objects with OrmLite.
 */
public class LocalDateTimeType extends BaseDateType {
    private static final LocalDateTimeType singleTon = new LocalDateTimeType();

    public static LocalDateTimeType getSingleton() {
        return singleTon;
    }

    private LocalDateTimeType() {
        super(SqlType.DATE, new Class<?>[] { LocalDateTime.class });
    }

    protected LocalDateTimeType(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
        DateStringFormatConfig dateFormatConfig = convertDateStringConfig(fieldType, getDefaultDateFormatConfig());
        try {
            return new Timestamp(parseDateString(dateFormatConfig, defaultStr).getTime());
        } catch (ParseException e) {
            throw SqlExceptionUtil.create("Problems parsing default date string '" + defaultStr + "' using '"
                    + dateFormatConfig + '\'', e);
        }
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getTimestamp(columnPos);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        Timestamp value = (Timestamp) sqlArg;

        return value.toLocalDateTime();
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        LocalDateTime date = (LocalDateTime) javaObject;

        return new Timestamp(DateUtil.toSqlDate(date).getTime());
    }

    @Override
    public boolean isArgumentHolderRequired() {
        return true;
    }

    /**
     * Return the default date format configuration.
     */
    protected DateStringFormatConfig getDefaultDateFormatConfig() {
        return defaultDateFormatConfig;
    }
}
