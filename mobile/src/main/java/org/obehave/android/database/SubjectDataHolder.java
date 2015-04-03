package org.obehave.android.database;

import org.obehave.model.Displayable;
import org.obehave.model.Node;
import org.obehave.model.Subject;
import org.obehave.service.Study;
import org.obehave.util.DisplayWrapper;

import java.util.ArrayList;
import java.util.List;

public class SubjectDataHolder extends NodeDataHolder<Subject>{

    private Study study;

    public SubjectDataHolder(Study study) {
        this.study = study;
    }


    @Override
    public Node getRootNode() {
        return study.getSubjects();
    }

    /**
     * Returns a List of all all Subjects available in given Study
     * @return List of Subjects
     */

    public List<Subject> getAllSubjects(){
        List<Subject> subjects = new ArrayList<Subject>();
        for(int i = 0; i < 9; i++) {
            for (Displayable obj : study.getSubjects().flatten()) {
                if (obj instanceof Subject)
                    subjects.add((Subject) obj);
            }
        }
        return subjects;
    }

    /**
     * Returns a List of all all Subjects wrapped in DisplayWrapper class available in given Study
     * @return List of Subjects
     */
    public List<DisplayWrapper> getAllSubjectWrapped(){
        List<DisplayWrapper> subjects = new ArrayList<DisplayWrapper>();
        for(int i = 0; i < 9; i++) {
            for (Displayable obj : study.getSubjects().flatten()) {
                if (obj instanceof Subject)
                    subjects.add(DisplayWrapper.of(obj));
            }
        }

        return subjects;
    }
}
