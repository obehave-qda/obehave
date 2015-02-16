package org.obehave.android.services;


import org.obehave.android.ui.exceptions.UiException;
import org.obehave.android.ui.util.DataHolder;
import org.obehave.model.Action;
import org.obehave.model.Subject;
import org.obehave.model.modifier.Modifier;
import org.obehave.model.modifier.ModifierFactory;

import java.util.Collections;
import java.util.List;

public class ApplicationService implements Service{

    private static List<Action> actions;
    private static List<Subject> subjects;

    public static void importFile(String filename){
        if(subjects == null){
            subjects = DataHolder.getInstance().getAllSubjects();
        }

        if(actions == null) {
            actions = DataHolder.getInstance().getAllActions();
        }
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
     * @returns ModifierFactory or Null
     * @throws UiException
     */
    public static ModifierFactory getModifierFactoryOfSelectedActions() throws UiException {
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

    }

    // the class should not be instanceable
    private ApplicationService(){

    }

    public static void reset(){
        actions = null;
        subjects = null;
    }

}
