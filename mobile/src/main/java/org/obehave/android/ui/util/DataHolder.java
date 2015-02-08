package org.obehave.android.ui.util;

import org.obehave.android.ui.exceptions.UiException;
import org.obehave.model.Action;
import org.obehave.model.Color;
import org.obehave.model.Subject;
import org.obehave.model.modifier.SubjectModifierFactory;

import java.util.ArrayList;
import java.util.List;

public class DataHolder {
    private static DataHolder instance = new DataHolder();
    private List<Subject> subjects;
    private List<Action> actions;

    public static DataHolder getInstance() {
        return instance;
    }

    private DataHolder() {
    }

    private int randomNumber(int min, int max){
        return min + (int)(Math.random()*max);
    }

    public List<Subject> getAllSubjects() throws UiException {
        if(subjects == null) {
            generateSubjects();
        }

        return subjects;
    }

    /**
     * Only for testing purpose.
     */
    private void generateSubjects(){
        subjects = new ArrayList<Subject>();

        String subjectNamens[] = {
                "Cherokee",
                "Amarok",
                "Kia",
                "Kim",
                "Kenai",
                "Kaspar",
                "Aragon",
                "Chitto",
                "Shima",
                "Tala",
                "Nanuk",
                "Una",
                "Geronimo",
                "Wamblee",
                "Yukon",
                "Apache",
                "Kay",
                "Tatonga",
                "Tayanita",
                "Wapi"

        };

        for(int i = 0; i < subjectNamens.length; i++){
            Subject subject = new Subject();
            subject.setName(subjectNamens[i]);
            subject.setAlias(subjectNamens[i].substring(0,2));
            subject.setColor(new Color(randomNumber(0,255), randomNumber(0,255), randomNumber(0,255)));
            subjects.add(subject);
        }
    }

    public List<Action> getAllActions() throws UiException {
        if(actions == null) {
            generateActions();
        }

        return actions;
    }

    /**
     *  Only for testing purpose.
     */
    public void generateActions(){
        actions = new ArrayList<Action>();

        String actionNames[] = {
                "spielen",
                "beißen",
                "schlafen",
                "heulen"
        };

        for(int i=0; i < actionNames.length; i++){
            Action action = new Action();
            action.setName(actionNames[i]);
            action.setType(Action.Type.POINT);
            action.setAlias(actionNames[i].substring(0,2));
            action.setRecurring(0);
            SubjectModifierFactory subjectModifierFactory = new SubjectModifierFactory();
            subjectModifierFactory.setName("Subject Modifier");
            subjectModifierFactory.setAlias("su");
            subjectModifierFactory.addValidSubjects(subjects.get(1), subjects.get(2), subjects.get(3));
            action.setModifierFactory(subjectModifierFactory);

            actions.add(action);
        }
    }
}
