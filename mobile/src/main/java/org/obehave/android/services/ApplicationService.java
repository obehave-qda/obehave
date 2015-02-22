package org.obehave.android.services;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.joda.time.DateTime;
import org.obehave.android.ui.events.TimerTaskEvent;
import org.obehave.android.ui.exceptions.UiException;
import org.obehave.android.ui.util.DataHolder;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Action;
import org.obehave.model.Subject;
import org.obehave.model.modifier.Modifier;
import org.obehave.model.modifier.ModifierFactory;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ApplicationService implements Service{

    private static final String LOG_TAG = ApplicationService.class.getSimpleName();
    private static List<Action> actions;
    private static List<Subject> subjects;
    private static DateTime startTime;
    private static Timer timer;
    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            ApplicationService.doTimerTasks();
        }
    };


    public static void importFile(String filename){
        if(subjects == null){
            subjects = DataHolder.getInstance().getAllSubjects();
        }

        if(actions == null) {
            actions = DataHolder.getInstance().getAllActions();
        }
    }

    public static boolean isTimerRunning(){
        return startTime != null;
    }

    public static void startTimer(){
        // check if timer is running, if the timer is running no action needed!
        if(!isTimerRunning()){
            startTime = new DateTime();
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    mHandler.obtainMessage(1).sendToTarget();
                }
            }, 0, 130);
            Log.d(LOG_TAG, "Timer started running!");
        }

    }

    public static void stopTimer(){
        if(isTimerRunning()){
            startTime = null;
            timer.cancel();
            timer = null;
            Log.d(LOG_TAG, "Timer stopped!");
        }
    }

    public static void doTimerTasks(){
        // update ui and such things!
        Log.d(LOG_TAG, "do Timer Task");
        EventBusHolder.post(new TimerTaskEvent(startTime));
    }

    public static List<Subject> getAllSubjects(){
        if(subjects == null) {
            return Collections.emptyList();
        }
        else {
            return subjects;
        }
    }

    public static  List<Action> getAllActions(){
        if(actions == null){
            return Collections.emptyList();
        }
        else {
            return actions;
        }
    }

    /**
     *
     * @returns ModifierFactory
     * @throws UiException
     */
    public static ModifierFactory getModifierFactoryOfSelectedAction() throws UiException {
        Action action = ApplicationState.getInstance().getAction();
        if(action == null){
            throw new UiException("Es wurde keine Aktion gewählt");
        }

        return action.getModifierFactory();
    }

    public static void selectItem(Subject subject){
        ApplicationState.getInstance().setSubject(subject);
    }

    public static void selectItem(Action action){
        ApplicationState.getInstance().setAction(action);
    }

    public static void selectItem(Modifier modifier){
        ApplicationState.getInstance().setModifier(modifier);
    }

    public static Subject getSelectedSubject(){
        return ApplicationState.getInstance().getSubject();
    }

    public static Action getSelectedAction(){
        return ApplicationState.getInstance().getAction();
    }

    public static Modifier getSelectedModifer(){
        return ApplicationState.getInstance().getModifier();
    }

    public static void createCoding() {
        // pass method to observation service?

    }

    // the class should not be instanceable
    private ApplicationService(){

    }

    public static void reset(){
        actions = null;
        subjects = null;
    }

    public static void onDestroy() {
        stopTimer();
    }
}
