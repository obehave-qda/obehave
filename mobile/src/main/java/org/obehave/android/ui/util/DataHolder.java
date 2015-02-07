package org.obehave.android.ui.util;

import org.obehave.android.ui.exceptions.UiException;
import org.obehave.model.Action;
import org.obehave.model.Color;
import org.obehave.model.Subject;

import java.util.ArrayList;
import java.util.Collections;
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
        if(subjects != null) {
            generateSubjects();
            return subjects;
        }

        return Collections.emptyList();
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
        if(actions != null) {
        }

        return Collections.emptyList();
    }
}
