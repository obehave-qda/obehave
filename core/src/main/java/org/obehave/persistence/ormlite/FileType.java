package org.obehave.persistence.ormlite;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;

import java.io.File;
import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class FileType extends BaseDataType {
    private static final FileType instance = new FileType();

    private FileType() {
        super(SqlType.STRING, new Class<?>[] { File.class });
    }

    public static FileType getInstance() {
        return instance;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        if (javaObject == null) {
            return null;
        } else {
            return ((File) javaObject).getAbsolutePath();
        }
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults databaseResults, int i) throws SQLException {
        return databaseResults.getString(i);
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String s) throws SQLException {
        return new File(s);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        return new File(sqlArg.toString());
    }
}
