package org.obehave.view.controller.components.tree;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.obehave.events.ChangeType;
import org.obehave.events.EventBusHolder;
import org.obehave.model.*;
import org.obehave.model.events.ActionChangeEvent;
import org.obehave.model.events.ObservationChangeEvent;
import org.obehave.model.events.SubjectChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class ProjectTreeComponent extends TreeView<String> {
    private static final Logger log = LoggerFactory.getLogger(ProjectTreeComponent.class);

    private Study study;

    private EventBus eventBus = EventBusHolder.getEventBus();

    private TreeItem root;
    private TreeItem subjectNode = new TreeItem<>("Subjects");
    private TreeItem actionNode = new TreeItem<>("Actions");
    private TreeItem observationsNode = new TreeItem<>("Observations");

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
            study.addRandomSubject(key);
        } else if (selectedItem == actionNode) {
            study.addRandomAction(key);
        } else if (selectedItem == observationsNode) {
            study.addRandomObservation(key);
        }
    }



    public void setStudy(Study study) {
        this.study = study;

        root = new TreeItem<>(study.getName());
        root.getChildren().addAll(subjectNode, actionNode, observationsNode);
        root.setExpanded(true);

        setRoot(root);
        setCellFactory(p -> new ContextMenuTreeCell(study));
    }

    @Subscribe
    public void handleSubjectChange(SubjectChangeEvent subjectChange) {
        String displayString = subjectChange.getChanged().getDisplayString();

        if (subjectChange.getChangeType() == ChangeType.CREATE) {
            addOnce(subjectNode, displayString);
        } else if (subjectChange.getChangeType() == ChangeType.DELETE) {
            subjectNode.getChildren().remove(getMatchingTreeItem(root, displayString));
        }
    }

    @Subscribe
    public void handleActionChange(ActionChangeEvent actionChange) {
        String displayString = actionChange.getChanged().getDisplayString();

        if (actionChange.getChangeType() == ChangeType.CREATE) {
            addOnce(actionNode, displayString);
        } else if (actionChange.getChangeType() == ChangeType.DELETE) {
            actionNode.getChildren().remove(getMatchingTreeItem(root, displayString));
        }
    }

    @Subscribe
    public void handleObservationChange(ObservationChangeEvent observationChange) {
        String displayString = observationChange.getChanged().getDisplayString();

        if (observationChange.getChangeType() == ChangeType.CREATE) {
            addOnce(observationsNode, displayString);
        } else if (observationChange.getChangeType() == ChangeType.DELETE) {
            observationsNode.getChildren().remove(getMatchingTreeItem(root, displayString));
        }
    }


    private Optional<TreeItem<String>> addOnce(TreeItem<String> treeItem, String text) {
        List<TreeItem<String>> children = treeItem.getChildren();
        TreeItem<String> entity = new TreeItem<>(text);
        for (TreeItem<String> item : children) {
            if (item.getValue().equals(entity.getValue())) {
                log.debug("Won't add another {} to the tree", entity.getValue());
                return Optional.empty();
            }
        }

        children.add(entity);
        return Optional.of(entity);
    }

    private TreeItem<String> getMatchingTreeItem(TreeItem<String> root, String text) {
        for (TreeItem<String> i : root.getChildren()) {
            if (i.getValue().equals(text)) {
                return i;
            }

            if (i.getChildren().size() > 0) {
                TreeItem<String> matchingItem = getMatchingTreeItem(i, text);
                if (matchingItem != null) {
                    return matchingItem;
                }
            }
        }

        return null;
    }
}
