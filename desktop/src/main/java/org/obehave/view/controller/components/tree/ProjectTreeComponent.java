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
import org.obehave.events.ChangeEvent;
import org.obehave.events.ChangeType;
import org.obehave.events.EventBusHolder;
import org.obehave.model.*;
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

        addEventHandler(KeyEvent.KEY_TYPED, this::addNewItem);
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
    public void changeEvent(ChangeEvent<?> change) {
        if (change.getChanged() instanceof Displayable) {
            Displayable d = (Displayable) change.getChanged();

            if (d instanceof Subject) {
                handleEntityChange(d, change.getChangeType(), subjectNode);
            } else if (d instanceof Action) {
                handleEntityChange(d, change.getChangeType(), actionNode);
            } else if (d instanceof Observation) {
                handleEntityChange(d, change.getChangeType(), observationsNode);
            }
        }
    }

    private void handleEntityChange(Displayable displayable, ChangeType changeType, TreeItem node) {
        String displayString = displayable.getDisplayString();

        if (changeType == ChangeType.CREATE) {
            addOnce(node, displayString);
        } else if (changeType == ChangeType.DELETE) {
            node.getChildren().remove(getMatchingTreeItem(root, displayString));
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
