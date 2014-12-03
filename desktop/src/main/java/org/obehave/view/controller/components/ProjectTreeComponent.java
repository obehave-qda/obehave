package org.obehave.view.controller.components;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyEvent;
import org.obehave.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ProjectTreeComponent extends TreeView<String> implements Observer {
    private static final Logger log = LoggerFactory.getLogger(ProjectTreeComponent.class);

    private Study study;

    private TreeItem root;
    private TreeItem<String> subjectNode = new TreeItem<>("Subjects");
    private TreeItem<String> actionNode = new TreeItem<>("Action");
    private TreeItem<String> observationsNode = new TreeItem<>("Observations");

    public ProjectTreeComponent() {
        super();
        subjectNode.setExpanded(true);
        actionNode.setExpanded(true);
        observationsNode.setExpanded(true);

        addEventHandler(KeyEvent.KEY_TYPED, event -> addNewItem(event));
    }

    private void addNewItem(KeyEvent event) {
        String key = event.getCharacter();
        TreeItem<String> selectedItem = getSelectionModel().getSelectedItem();
        if (selectedItem == subjectNode) {
            addRandomSubject(key);
        } else if (selectedItem == actionNode) {
            addRandomAction(key);
        } else if (selectedItem == observationsNode) {
            addRandomObservation(key);
        }
    }

    private void addRandomSubject(String key) {
        study.addSubject(new Subject(getRandomString("Subject " + key)));
    }

    private void addRandomAction(String key) {
        study.addAction(new Action(getRandomString("Action " + key)));
    }

    private void addRandomObservation(String key) {
        study.addObservation(new Observation(getRandomString("Observation " + key)));
    }

    private String getRandomString(String prefix) {
        int number = (int) (Math.random() * 5);
        return prefix + " " + number;
    }

    public void setStudy(Study study) {
        this.study = study;
        study.addObserver(this);

        root = new TreeItem<>(study.getName());
        root.getChildren().addAll(subjectNode, actionNode, observationsNode);
        root.setExpanded(true);

        setRoot(root);
    }

    @Override
    public void update(Observable o, Object changedObject) {
        if (changedObject instanceof Displayable) {
            Displayable displayable = (Displayable) changedObject;
            String displayString = displayable.getDisplayString();
            log.debug("Something has changed - adding {}", displayString);

            if (displayable instanceof Subject) {
                TreeItem<String> ti = new TreeItem<>(displayString);
                addOnce(subjectNode.getChildren(), ti);
            } else if (displayable instanceof Action) {
                TreeItem<String> ti = new TreeItem<>(displayString);
                addOnce(actionNode.getChildren(), ti);
            } else if (displayable instanceof Observation) {
                TreeItem<String> ti = new TreeItem<>(displayString);
                addOnce(observationsNode.getChildren(), ti);
            }
        }
    }

    private void addOnce(List<TreeItem<String>> children, TreeItem<String> entity) {
        for (TreeItem<String> item : children) {
            if (item.getValue().equals(entity.getValue())) {
                log.debug("Won't add another {}", entity.getValue());
                return;
            }
        }

        children.add(entity);
    }
}
