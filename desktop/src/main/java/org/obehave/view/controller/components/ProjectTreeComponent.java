package org.obehave.view.controller.components;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.obehave.model.Study;

public class ProjectTreeComponent extends TreeView<String> {
    private Study study;

    public ProjectTreeComponent() {
        super();
    }

    public void setStudy(Study study) {
        this.study = study;
        updateTree();
    }

    @FXML
    public void updateTree() {
        TreeItem<String> root = new TreeItem<>("Study Xyz");
        TreeItem<String> subjects = new TreeItem<>("Subjects");
        TreeItem<String> actions = new TreeItem<>("Action");
        TreeItem<String> observations = new TreeItem<>("Observations");

        root.getChildren().addAll(subjects, actions, observations);
        root.setExpanded(true);

        setRoot(root);
    }
}
