package org.obehave.android.application;

import android.app.Application;
import android.content.Context;
import org.obehave.android.ui.exceptions.UiException;
import org.obehave.android.ui.fragments.behaviors.SortType;
import org.obehave.android.util.DateTimeHelper;
import org.obehave.exceptions.ServiceException;
import org.obehave.exceptions.Validate;
import org.obehave.exceptions.ValidationException;
import org.obehave.model.Observation;
import org.obehave.service.CodingService;
import org.obehave.service.NodeService;
import org.obehave.service.ObservationService;
import org.obehave.service.Study;
import org.obehave.util.I18n;

import java.io.File;
import java.sql.SQLException;

public class MyApplication extends Application {
    private static final String LOG_TAG = MyApplication.class.getSimpleName();
    private static Context context;

    private Study study;
    private Observation observation;
    private BackStackHistory backStackHistory;

    // stores the current selection (subject, action, modifier, timestamp
    private CodingState codingState;
    private int actionSortOrder = SortType.DEFAULT;
    private int subjectSortOrder = SortType.DEFAULT;

    public void onCreate(){
        super.onCreate();
        MyApplication.context = getApplicationContext();
        codingState = new CodingState();
        backStackHistory = new BackStackHistory();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public Study getStudy() {
        return study;
    }

    public void loadStudy(String srcFilename) throws UiException {
        try {
            // when we load a new study we have to close the observation
            study = Study.load(new File(srcFilename));
            observation = null;
        } catch (SQLException e) {
            throw new UiException(I18n.get("android.ui.study.error.studycannotbeloaded"), e);
        }
    }

    public void setObservation(Observation observation) {
        this.observation = observation;
    }

    public Observation getObservation() {
        return observation;
    }

    public ObservationService getObservationService() {
        Validate.isNotNull(study, "Study");
        return study.getObservationService();
    }

    public NodeService getNodeService(){
        Validate.isNotNull(study, "Study");
        return study.getNodeService();
    }

    public CodingService getCodingService() {
        Validate.isNotNull(study, "Study");
        Validate.isNotNull(observation, "Observation");
        return study.getCodingServiceBuilder().build(observation);
    }

    public CodingState getCodingState() {
        return codingState;
    }

    public void createCoding() throws UiException {
        try {
            Validate.isNotNull(getCodingState(), "CodingState");
            Validate.isNotNull(getCodingState().getStartTime(), "StartTime");
            Validate.isNotNull(getCodingState().getSubject(), "Subject");
            Validate.isNotNull(getCodingState().getAction(), "Action");
        }catch(ValidationException e){
            throw new UiException(I18n.get("android.ui.coding.create.error"), e);
        }

        try {
            String modifierBuildString = null;
            if(getCodingState().getModifier() != null){
                modifierBuildString = getCodingState().getModifier().getBuildString();
            }

            getCodingService().startCoding(getCodingState().getSubject(),
                    getCodingState().getAction(),
                    modifierBuildString,
                    DateTimeHelper.diffMs(getObservation().getDateTime(), getCodingState().getStartTime()));

        } catch (ServiceException e) {
            throw new UiException(I18n.get("android.ui.coding.create.error"), e);
        }
    }

    public void resetCodingState() {
        codingState = new CodingState();
    }

    public void setActionSortOrder(int actionSortOrder) {
        this.actionSortOrder = actionSortOrder;
    }

    public int getActionSortOrder() {
        return actionSortOrder;
    }

    public void setSubjectSortOrder(int subjectSortOrder) {
        this.subjectSortOrder = subjectSortOrder;
    }

    public int getSubjectSortOrder() {
        return subjectSortOrder;
    }

    public BackStackHistory getBackStackHistory() {
        return backStackHistory;
    }
}
