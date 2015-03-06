package org.obehave.android.database;

import org.obehave.android.ui.exceptions.UiException;
import org.obehave.service.Study;

import java.io.File;
import java.sql.SQLException;

public class DataHolder {

    /**
     * Holds the loaded Study
     */
    private static Study study;
    private static SubjectDataHolder subjectDataHolder;
    private static ActionDataHolder actionDataHolder;

    /**
     * Private Constructor - Class keeps static not instanceable
     */
    private DataHolder() {
    }

    /**
     * Holds the SubjectDataHolder
     * @param filename
     * @throws UiException
     */

    public static void loadStudy(String filename) throws UiException {
        if(study == null) {
            try {
                File file = new File(filename);
                final boolean isAndroid = true;
                study.load(new File(filename), isAndroid);
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
        if(actionDataHolder == null) {
            return new ActionDataHolder(study);
        }

        return actionDataHolder;
    }

}
