package org.obehave.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.obehave.model.Action;
import org.obehave.model.Observation;
import org.obehave.model.Study;
import org.obehave.model.Subject;

import java.sql.SQLException;

public class MyDatabaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String DATABASE_NAME = "obehave.db";
    private static final int DATABASE_VERSION = 1;
    private final String LOG_TAG = getClass().getSimpleName();

    private Dao<Study, Long> studyDao;
    private Dao<Subject, Long> subjectDao;
    private Dao<Action, Long> actionDao;
    private Dao<Observation, Long> observationDao;
    //private Dao<SubjectGroup, Long> subjectGroupDao;
    //private Dao<ActionGroup, Long> actionGroupDao;


    public MyDatabaseHelper(Context context, String dbFilename){
        super(context, dbFilename, null, DATABASE_VERSION);
        Log.d(LOG_TAG, "database helper created");
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.d(LOG_TAG, "db created");
            createTables(db, connectionSource);
            createTestEntries();
        } catch (SQLException e){
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    private void createTables(SQLiteDatabase db, ConnectionSource connectionSource1) throws SQLException {
        Log.i(LOG_TAG, "create tables");
        TableUtils.createTable(connectionSource, Subject.class);
        TableUtils.createTable(connectionSource, Action.class);
        TableUtils.createTable(connectionSource, Observation.class);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            dropTables(db, connectionSource);
        } catch (SQLException e) {
            Log.i(LOG_TAG, "onUpgrade");
            Log.i(LOG_TAG, "Exception thrown: " + e.getMessage());
            e.printStackTrace();
        }
        onCreate(db, connectionSource);
    }

    private void dropTables(SQLiteDatabase db, ConnectionSource connectionSource) throws SQLException {
        boolean ignoreErrors = true;
        TableUtils.dropTable(connectionSource, Subject.class, ignoreErrors);
        TableUtils.dropTable(connectionSource, Observation.class, ignoreErrors);
        TableUtils.dropTable(connectionSource, Action.class, ignoreErrors);
    }

    public Dao<Study, Long> getStudyDao() throws SQLException {
        if(studyDao == null){
            studyDao = getDao(Study.class);
        }
        return studyDao;
    }

    public Dao<Subject, Long> getSubjectDao() throws SQLException {
        if(subjectDao == null){
            subjectDao = getDao(Subject.class);
        }

        return subjectDao;
    }

    public Dao<Action, Long> getActionDao() throws SQLException{
        if(actionDao == null){
            actionDao = getDao(Action.class);
        }
        return actionDao;
    }



    private void createTestEntries() throws SQLException {;
            generateTestEntries();
    }



    private void generateTestEntries() throws SQLException {
        Subject subject;

        for(int i = 0; i < 30; i++) {
            subject = new Subject("Subject " + i);
            Action action = new Action("Action " + i);
            Dao<Subject, Long> subjectDao = getSubjectDao();
            Dao<Action, Long> actionDao = getActionDao();
            subjectDao.create(subject);
            actionDao.create(action);
        }
    }

}
