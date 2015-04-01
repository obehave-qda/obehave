package org.obehave.android.application;


import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.joda.time.DateTime;
import org.obehave.android.database.DataHolder;
import org.obehave.android.ui.events.TimerTaskEvent;
import org.obehave.android.ui.exceptions.UiException;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Action;
import org.obehave.model.Coding;
import org.obehave.model.Subject;
import org.obehave.model.modifier.Modifier;
import org.obehave.model.modifier.ModifierFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyApplication  extends Application{

    private static final String LOG_TAG = MyApplication.class.getSimpleName();
    private static final int INTERVAL_TIME = 130;
    /**
     * Stores the time when the timer is started
     */
    private static DateTime startTime;
    /**
     * Interval timer
     */
    private static Timer timer;
    /**
     * Callback function for timer, get called every x milliseconds
     */
    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            MyApplication.doTimerTasks();
        }
    };

    /**
     * List of all Codings
     */
    private List<Coding> allCodings;

    private static Context context;

    public void onCreate(){
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public static void loadFile(String filename) throws UiException {
        DataHolder.loadStudy(filename);
    }

    /**
     * Checks if the timer is running.
     * @return boolean
     */
    public static boolean isTimerRunning(){
        return startTime != null;
    }

    /** starts Timer
     *
     */
    public static void startTimer(){
        // check if timer is running, if the timer is running no action needed!
        if(!isTimerRunning()){
            startTime = new DateTime();
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    mHandler.obtainMessage(1).sendToTarget();
                }
            }, 0, INTERVAL_TIME);
            Log.d(LOG_TAG, "Timer started running!");
        }

    }
    /**
     * Stops the running Timer
     */
    public static void stopTimer(){
        if(isTimerRunning()){
            startTime = null;
            timer.cancel();
            timer = null;
            Log.d(LOG_TAG, "Timer stopped!");
        }
    }

    /**
     * fires event every x milliseconds.
     */
    public static void doTimerTasks(){
        // update ui and such things!
        Log.d(LOG_TAG, "do Timer Task");
        EventBusHolder.post(new TimerTaskEvent(startTime));
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
        ApplicationState.getInstance().setCodingStartedTime();
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

    public static void setSubjectSortOrder(int sortType){
        ApplicationState.getInstance().setSelectedSubjectSortType(sortType);
    }

    public static int getSubjectSortOrder(){
        return ApplicationState.getInstance().getSelectedSubjectSortType();
    }

    public static void setActionSortOrder(int sortType){
        ApplicationState.getInstance().setSelectedActionSortType(sortType);
    }

    public static int getActionSortOrder(){
        return ApplicationState.getInstance().getSelectedActionSortType();
    }

    public static void createCoding() {
        // pass method to observation service?

    }

    public MyApplication(){

    }

    public static void onDestroy() {
        stopTimer();
    }
}
