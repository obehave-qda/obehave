package org.obehave.android.database;

import org.obehave.android.ui.exceptions.UiException;
import org.obehave.exceptions.Validate;
import org.obehave.service.Study;

import java.io.File;
import java.sql.SQLException;

public class DataHolder {

    /**
     * Holds the loaded Study
     */
    private static final String DB_FOLDER = "databases";
    private static Study study;
    private static SubjectDataHolder subjectDataHolder;
    private static ActionDataHolder actionDataHolder;

    /**
     * Private Constructor - Class keeps static not instanceable
     */
    private DataHolder() {
    }

    public static boolean isStudyLoaded(){
        return study != null;
    }

    /**
     * Holds the SubjectDataHolder
     * @param srcFilename
     * @throws UiException
     */

    public static void loadStudy(String srcFilename) throws UiException {
        if(study == null) {
            try {
                File srcFile = new File(srcFilename);
                final boolean isAndroid = true;
                study = study.load(new File(srcFilename), isAndroid);
            } catch (SQLException e) {
                // TODO: I18n
                throw new UiException("Die Study konnte nicht geladen werden.", e);
            }
        }
    }


    /**
     * Creates a SubjectDataHolder instance if currently none exists.
     * If a instances exists, than we will return the stored instance.
     *
     * @return SubjectDataHolder
     */
    public static SubjectDataHolder subject(){
        Validate.isNotNull(study, "Study");
        if(subjectDataHolder == null) {
            return new SubjectDataHolder(study);
        }

        return subjectDataHolder;
    }

    /**
     * Creates a ActionDataHolder instance if currently none exists.
     * If a instances exists, than we will return the stored instance.
     *
     * @return ActionDataHolder
     */
    public static ActionDataHolder action(){
        Validate.isNotNull(study, "Study");
        if(actionDataHolder == null) {
            return new ActionDataHolder(study);
        }

        return actionDataHolder;
    }

    public static Study getStudy(){
        return study;
    }

}
