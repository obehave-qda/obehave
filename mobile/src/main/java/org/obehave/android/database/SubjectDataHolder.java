package org.obehave.android.database;

import org.obehave.model.Node;
import org.obehave.model.Subject;
import org.obehave.service.Study;

/**
 * Created by patrick on 03.03.2015.
 */
public class SubjectDataHolder extends NodeDataHolder<Subject>{

    private Study study;

    public SubjectDataHolder(Study study) {
        this.study = study;
    }


    @Override
    public Node getRootNode() {
        return study.getSubjects();
    }
}
