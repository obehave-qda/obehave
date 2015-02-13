package org.obehave.persistence.ormlite;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;
import org.obehave.model.Color;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class ColorType extends BaseDataType {
    private static final ColorType instance = new ColorType();

    private ColorType() {
        super(SqlType.STRING, new Class<?>[] { Color.class });
    }

    public static ColorType getInstance() {
        return instance;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        if (javaObject == null) {
            return null;
        } else {
            return javaObject.toString();
        }
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults databaseResults, int i) throws SQLException {
        return databaseResults.getString(i);
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String s) throws SQLException {
        return Color.valueOf(s);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        return Color.valueOf(sqlArg.toString());
    }
}
