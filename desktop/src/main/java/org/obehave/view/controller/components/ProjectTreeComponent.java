package org.obehave.view.controller.components;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.obehave.model.*;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ProjectTreeComponent extends TreeView<String> implements Observer {
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

        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> addThing(event));
    }

    private void addThing(MouseEvent event) {
        Node node = event.getPickResult().getIntersectedNode();
        if (node instanceof Text || node instanceof TreeCell) {
            TreeItem<String> selectedItem = getSelectionModel().getSelectedItem();
            if (selectedItem == subjectNode) {
                addRandomSubject();
            } else if (selectedItem == actionNode) {
                addRandomAction();
            } else if (selectedItem == observationsNode) {
                addRandomObservation();
            }
        }
    }

    private void addRandomSubject() {
        study.addSubject(new Subject(getRandomString("Subject")));
    }

    private void addRandomAction() {
        study.addAction(new Action(getRandomString("Action")));
    }

    private void addRandomObservation() {
        study.addObservation(new Observation(getRandomString("Observation")));
    }

    private String getRandomString(String prefix) {
        int number = (int) (Math.random() * 100);
        return prefix + " " + number;
    }

    public void setStudy(Study study) {
        this.study = study;
        study.addObserver(this);

        root = new TreeItem<>(study.getName());
        root.getChildren().addAll(subjectNode, actionNode, observationsNode);
        root.setExpanded(true);

        setRoot(root);

        updateTree();
    }

    @FXML
    public void updateTree() {
        Subject s1 = new Subject("Wolf A");
        Subject s2 = new Subject("Wolf B");
        Action a1 = new Action("Running");
        Action a2 = new Action("Sleeping");
        Observation o1 = new Observation("Observation 1");
        Observation o2 = new Observation("Observation 2");

        study.addSubject(s1);
        study.addSubject(s2);
        study.addAction(a1);
        study.addAction(a2);
        study.addObservation(o1);
        study.addObservation(o2);
    }

    @Override
    public void update(Observable o, Object changedObject) {
        if (changedObject instanceof Displayable) {
            Displayable displayable = (Displayable) changedObject;

            if (displayable instanceof Subject) {
                TreeItem<String> ti = new TreeItem<>(displayable.getDisplayString());
                addOnce(subjectNode.getChildren(), ti);
            } else if (displayable instanceof Action) {
                TreeItem<String> ti = new TreeItem<>(displayable.getDisplayString());
                addOnce(actionNode.getChildren(), ti);
            } else if (displayable instanceof Observation) {
                TreeItem<String> ti = new TreeItem<>(displayable.getDisplayString());
                addOnce(observationsNode.getChildren(), ti);
            }
        }
    }

    private <T> void addOnce(List<T> children, T entity) {
        if (!children.contains(entity)) {
            children.add(entity);
        }
    }
}
