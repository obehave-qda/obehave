package org.obehave.android.ui.util;

import com.j256.ormlite.dao.Dao;
import org.obehave.android.database.MyDatabaseHelper;
import org.obehave.android.ui.exceptions.UiException;
import org.obehave.model.Subject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataHolder {
    private static DataHolder ourInstance = new DataHolder();
    List<Subject> subjects;

    public static DataHolder getInstance() {
        return ourInstance;
    }

    private DataHolder() {
    }

    public List<Subject> getAllSubjects(MyDatabaseHelper helper) throws UiException {
        if(subjects != null) {
            try {
                Dao<Subject, Long> subjectDao = helper.getSubjectDao();
                subjects = subjectDao.queryForAll();
            } catch (SQLException e) {
                // TODO: locale Exception!
                throw new UiException("Es ist ein schwerer Fehler aufgetreten!", e);
            }

            subjects = new ArrayList<>();
        }

        return subjects;
    }
}
