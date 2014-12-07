package org.obehave.view.controller.components.tree;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyEvent;
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

    private TreeItem root;
    private TreeItem subjectNode = new TreeItem<>("Subjects");
    private TreeItem actionNode = new TreeItem<>("Actions");
    private TreeItem observationsNode = new TreeItem<>("Observations");

    public ProjectTreeComponent() {
        super();
        subjectNode.setExpanded(true);
        actionNode.setExpanded(true);
        observationsNode.setExpanded(true);

        EventBusHolder.getEventBus().register(this);

        addEventHandler(KeyEvent.KEY_TYPED, this::addNewItem);
    }

    /**
     * A method only meant for testing purpouses - this will add a new item when a key was pressed
     *
     * @param event the keyevent, where the keycode will be read from
     */
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

    /**
     * Add a new item to the tree, if it isn't there already.
     * <p/>
     * This kind of check should be done in the study itself, not in the view
     *
     * @param treeItem the treeitem to add
     * @param text     the text to look for
     * @return an optional TreeItem once it's added - if it wasn't there already
     */
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

    /**
     * Searches for a item, where the displayed value is equal to a given text
     *
     * @param root the node where to start the search from
     * @param text the text to search
     * @return a matching item
     */
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
