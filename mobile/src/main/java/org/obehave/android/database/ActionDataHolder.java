package org.obehave.android.database;

import org.obehave.model.Node;
import org.obehave.service.Study;


public class ActionDataHolder extends NodeDataHolder{

    private Study study;

    public ActionDataHolder(Study study) {
        this.study = study;
    }

    @Override
    public Node getRootNode() {
        return study.getActions();
    }
}
