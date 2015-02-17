package org.obehave.android.ui.util;

import org.obehave.model.Action;
import org.obehave.model.Color;
import org.obehave.model.Subject;
import org.obehave.model.modifier.ModifierFactory;

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

    public List<Subject> getAllSubjects() {
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

    public List<Action> getAllActions() {
        if(actions == null) {
            generateActions();
        }

        return actions;
    }

    /**
     *  Only for testing purpose.
     */

    private void generateActions(){
        actions = new ArrayList<Action>();
        generateNumberRangeActions();
        generateSubjectModifierFactoryActions();
        generateEnumerationModifierActions();
    }

    private void generateSubjectModifierFactoryActions(){


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
            ModifierFactory subjectModifierFactory = new ModifierFactory(subjects.get(1), subjects.get(2), subjects.get(3));
            subjectModifierFactory.setName("Subject Modifier");
            subjectModifierFactory.setAlias("su");
            action.setModifierFactory(subjectModifierFactory);

            actions.add(action);
        }
    }

    private void generateNumberRangeActions(){
        String actionNames[] = {
                "number 1",
                "number 2",
                "number 3"
        };

        for(int i=0; i < actionNames.length; i++){
            Action action = new Action();
            action.setName(actionNames[i]);
            action.setType(Action.Type.POINT);
            action.setAlias(actionNames[i].substring(0,2));
            action.setRecurring(0);
            ModifierFactory decimalRangeModifierFactory = new ModifierFactory(20,100);
            decimalRangeModifierFactory.setName("Decimal Modifier");
            decimalRangeModifierFactory.setAlias("su");
            action.setModifierFactory(decimalRangeModifierFactory);

            actions.add(action);
        }
    }

    private void generateEnumerationModifierActions(){


        String actionNames[] = {
                "enum 1",
                "enum 2",
                "enum 3"
        };

        for(int i=0; i < actionNames.length; i++){
            Action action = new Action();
            action.setName(actionNames[i]);
            action.setType(Action.Type.POINT);
            action.setAlias(actionNames[i].substring(0,2));
            action.setRecurring(0);
            ModifierFactory enumerationModifierFactory = new ModifierFactory("Value1", "Value 2", "Value 3", "Value4");
            enumerationModifierFactory.setName("Enumeration Modifier");
            enumerationModifierFactory.setAlias("enu");
            action.setModifierFactory(enumerationModifierFactory);

            actions.add(action);
        }
    }
}
