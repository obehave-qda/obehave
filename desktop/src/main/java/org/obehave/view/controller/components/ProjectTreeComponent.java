package org.obehave.view.controller.components;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyEvent;
import org.obehave.events.EventBusHolder;
import org.obehave.model.*;
import org.obehave.model.events.ActionChangeEvent;
import org.obehave.model.events.ObservationChangeEvent;
import org.obehave.model.events.SubjectChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Observable;

public class ProjectTreeComponent extends TreeView<String> {
    private static final Logger log = LoggerFactory.getLogger(ProjectTreeComponent.class);

    private Study study;

    private EventBus eventBus = EventBusHolder.getEventBus();

    private TreeItem root;
    private TreeItem<String> subjectNode = new TreeItem<>("Subjects");
    private TreeItem<String> actionNode = new TreeItem<>("Action");
    private TreeItem<String> observationsNode = new TreeItem<>("Observations");

    public ProjectTreeComponent() {
        super();
        subjectNode.setExpanded(true);
        actionNode.setExpanded(true);
        observationsNode.setExpanded(true);

        eventBus.register(this);

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

        root = new TreeItem<>(study.getName());
        root.getChildren().addAll(subjectNode, actionNode, observationsNode);
        root.setExpanded(true);

        setRoot(root);
    }

    @Subscribe
    public void handleSubjectChange(SubjectChangeEvent subjectChange) {
        String displayString = subjectChange.getChanged().getDisplayString();
        log.debug("Something has changed - adding {}", displayString);

        addOnce(subjectNode, displayString);
    }

    @Subscribe
    public void handleActionChange(ActionChangeEvent subjectChange) {
        String displayString = subjectChange.getChanged().getDisplayString();
        log.debug("Something has changed - adding {}", displayString);

        addOnce(actionNode, displayString);
    }

    @Subscribe
    public void handleObservationChange(ObservationChangeEvent subjectChange) {
        String displayString = subjectChange.getChanged().getDisplayString();
        log.debug("Something has changed - adding {}", displayString);

        addOnce(observationsNode, displayString);
    }


    private void addOnce(TreeItem<String> treeItem, String text) {
        List<TreeItem<String>> children = treeItem.getChildren();
        TreeItem<String> entity = new TreeItem<>(text);
        for (TreeItem<String> item : children) {
            if (item.getValue().equals(entity.getValue())) {
                log.debug("Won't add another {}", entity.getValue());
                return;
            }
        }

        children.add(entity);
    }
}
