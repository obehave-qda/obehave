package org.obehave.persistence.ormlite;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class ClassType extends BaseDataType {
    private static final ClassType instance = new ClassType();

    private ClassType() {
        super(SqlType.STRING, new Class<?>[] { Class.class });
    }

    public static ClassType getInstance() {
        return instance;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        if (javaObject == null) {
            return null;
        } else {
            return ((Class) javaObject).getName();
        }
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults databaseResults, int i) throws SQLException {
        return databaseResults.getString(i);
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String s) throws SQLException {
        try {
            return Class.forName(String.valueOf(s));
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        try {
            return Class.forName(String.valueOf(sqlArg));
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
    }
}