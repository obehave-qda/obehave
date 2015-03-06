package org.obehave.view.controller.components.tree;

import com.google.common.eventbus.Subscribe;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.obehave.events.ChangeEvent;
import org.obehave.events.ChangeType;
import org.obehave.events.EventBusHolder;
import org.obehave.model.*;
import org.obehave.service.Study;
import org.obehave.util.DisplayWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProjectTreeComponent extends TreeView<String> {
    private static final Logger log = LoggerFactory.getLogger(ProjectTreeComponent.class);

    private Study study;

    private TreeItem root;
    private TreeItem<DisplayWrapper<Node>> subjectNode;
    private TreeItem<DisplayWrapper<Node>> actionNode;
    private TreeItem<DisplayWrapper<Node>> modifierFactoryNode;
    private TreeItem<DisplayWrapper<Node>> observationsNode;

    public ProjectTreeComponent() {
        super();

        EventBusHolder.register(this);
    }

    public void setStudy(Study study) {
        this.study = study;

        redoTree();
    }

    private void redoTree() {
        root = new TreeItem<>(DisplayWrapper.of(study.getName()));

        subjectNode = createTreeItem(study.getSubjects());
        actionNode = createTreeItem(study.getActions());
        modifierFactoryNode = createTreeItem(study.getModifierFactories());
        observationsNode = createTreeItem(study.getObservations());

        root.getChildren().addAll(subjectNode, actionNode, modifierFactoryNode, observationsNode);
        root.setExpanded(true);

        setRoot(root);
    }

    private TreeItem<DisplayWrapper<Node>> createTreeItem(Node node) {
        TreeItem<DisplayWrapper<Node>> treeItem = new TreeItem<>();
        treeItem.setExpanded(true);
        treeItem.setValue(DisplayWrapper.of(node));

        List<Node> children = node.getChildren();

        for (Node child : children) {
            treeItem.getChildren().add(createTreeItem(child));
        }

        return treeItem;
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
            node.getChildren().add(new TreeItem<>(displayString));
        } else if (changeType == ChangeType.DELETE) {
            node.getChildren().remove(getMatchingTreeItem(root, displayString));
        }
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
