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
import org.obehave.model.Subject;
import org.obehave.service.Study;

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


    public MyDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        TableUtils.createTable(connectionSource, Study.class);
        TableUtils.createTable(connectionSource, Subject.class);
        TableUtils.createTable(connectionSource, Action.class);
        TableUtils.createTable(connectionSource, Observation.class);
        //TableUtils.createTable(connectionSource, SubjectGroup.class);
        //TableUtils.createTable(connectionSource, ActionGroup.class);;
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
        TableUtils.dropTable(connectionSource, Study.class, ignoreErrors);
        TableUtils.dropTable(connectionSource, Subject.class, ignoreErrors);
        TableUtils.dropTable(connectionSource, Observation.class, ignoreErrors);
        TableUtils.dropTable(connectionSource, Action.class, ignoreErrors);
        //TableUtils.dropTable(connectionSource, SubjectGroup.class, ignoreErrors);
        //TableUtils.dropTable(connectionSource, ActionGroup.class, ignoreErrors);
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
/*
    public Dao<ActionGroup, Long> getActionGroupDao() throws SQLException{
        if(actionGroupDao == null){
            actionGroupDao = getDao(ActionGroup.class);
        }
        return actionGroupDao;
    }
    */

    public Dao<Action, Long> getObservationDao() throws SQLException{
        if(observationDao == null){
            observationDao = getDao(Observation.class);
        }
        return actionDao;
    }
/*
    public Dao<SubjectGroup, Long> getSubjectGroupDao() throws SQLException{
        if(subjectGroupDao == null){
            subjectGroupDao = getDao(SubjectGroup.class);
        }
        return subjectGroupDao;
    }
    */

    private void createTestEntries() throws SQLException {
            Study study = Study.create(null);
            study.setName("Test Study");
          //  generateTestEntries(study);
            getStudyDao().create(study);
    }
/*
    private void generateTestEntries(Study study) throws SQLException {
        Subject subject;
        study.setName("Android TestStudy");
        SubjectGroup sg1 = new SubjectGroup("Gruppe 1");
        SubjectGroup sg2 = new SubjectGroup("Gruppe 2");
        Dao<SubjectGroup, Long> subjectGroupDao = getSubjectGroupDao();
        subjectGroupDao.create(sg1);
        subjectGroupDao.create(sg2);

        Dao<ActionGroup, Long> actionGroupDao = getActionGroupDao();
        ActionGroup ag1 = new ActionGroup("ActionGroup 1");
        ActionGroup ag2 = new ActionGroup("ActionGroup 2");
        actionGroupDao.create(ag1);
        actionGroupDao.create(ag2);

        for(int i = 0; i < 30; i++) {

            subject = new Subject("Subject " + i);
            Action action = new Action("Action " + i);
            if(i < 15){
                subject.setSubjectGroup(sg1);
                action.setActionGroup(ag1);
            }
            else {
                subject.setSubjectGroup(sg2);
                action.setActionGroup(ag2);
            }
            Dao<Subject, Long> subjectDao = getSubjectDao();
            Dao<Action, Long> actionDao = getActionDao();
            subjectDao.create(subject);
            actionDao.create(action);
            study.addSubject(subject);
            study.addAction(action);
        }
        Dao<Subject, Long> subjectDao = getSubjectDao();
        subject = new Subject("Subject 40");
        subject.setSubjectGroup(null);
        subjectDao.create(subject);
        subject = new Subject("Subject 41");
        subject.setSubjectGroup(null);
        subjectDao.create(subject);
    }

*/
}
