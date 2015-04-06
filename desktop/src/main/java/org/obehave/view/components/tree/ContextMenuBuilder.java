package org.obehave.view.components.tree;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Action;
import org.obehave.model.Node;
import org.obehave.service.Study;
import org.obehave.util.DisplayWrapper;
import org.obehave.view.util.AlertUtil;

import java.util.function.Supplier;

/**
 * @author Markus Möslinger
 */
public class ContextMenuBuilder {
    private final PopOverHolder popOverHolder;
    private Node node;
    private int hierarchyLevel;
    private Supplier<javafx.scene.Node> nodeAnchor;

    private Study study;

    private ContextMenu cm = new ContextMenu();

    private ContextMenuBuilder(PopOverHolder popOverHolder) {
        this.popOverHolder = popOverHolder;
    }

    public static ContextMenu forItem(Study study, PopOverHolder popOverHolder,
                                      TreeItem<DisplayWrapper<?>> treeItem,
                                      Supplier<javafx.scene.Node> ownerNodeSupplier) {
        ContextMenuBuilder builder = new ContextMenuBuilder(popOverHolder);
        builder.study = study;

        builder.node = (Node) treeItem.getValue().get();
        builder.hierarchyLevel = getHierarchyLevel(treeItem);
        builder.nodeAnchor = ownerNodeSupplier;

        builder.addNewItem();
        builder.addNewGroup();
        builder.addEditItem();
        builder.addEditGroup();
        builder.addDeleteItem();

        return builder.cm;
    }

    private void addNewGroup() {
        if (node.getData() == null && node.getDataType() == Action.class && hierarchyLevel == 1) {
            final MenuItem menuItem = new MenuItem("Add group");
            menuItem.setOnAction(event -> popOverHolder.getNewActionGroupWithParent(node).show(nodeAnchor.get()));

            cm.getItems().add(menuItem);
        }
    }

    private void addNewItem() {
        if (node.getData() == null && (hierarchyLevel == 1 || (node.getDataType() == Action.class && hierarchyLevel == 2))) {
            final MenuItem menuItem = new MenuItem("Add item");

            if (node.getDataType() == Action.class && node.getData() == null) {
                menuItem.setOnAction(event -> popOverHolder.getActionNew(node).show(nodeAnchor.get()));
            } else {
                menuItem.setOnAction(event -> popOverHolder.get(node).show(nodeAnchor.get()));
            }

            cm.getItems().add(menuItem);
        }
    }

    private void addEditGroup() {
        if (node.getData() == null && node.getDataType() == Action.class && hierarchyLevel == 2) {
            final MenuItem menuItem = new MenuItem("Edit group");
            menuItem.setOnAction(event -> popOverHolder.get(node).show(nodeAnchor.get()));

            cm.getItems().add(menuItem);
        }
    }

    private void addEditItem() {
        if (node.getData() != null) {
            final MenuItem menuItem = new MenuItem("Edit item");
            menuItem.setOnAction(event -> popOverHolder.get(node).show(nodeAnchor.get()));

            cm.getItems().add(menuItem);
        }
    }

    private void addDeleteItem() {
        if (node.getData() != null) {
            final MenuItem menuItem = new MenuItem("Delete item");
            menuItem.setOnAction(event -> {
                node.getParent().remove(node);
                try {
                    study.getNodeService().save(node.getParent());
                    study.getNodeService().delete(node);
                } catch (ServiceException exception) {
                    AlertUtil.showError("Error", exception.getMessage());
                }
            });

            cm.getItems().add(menuItem);
        }
    }

    private static int getHierarchyLevel(TreeItem<?> treeItem) {
        int level = 0;

        while (treeItem.getParent() != null) {
            treeItem = treeItem.getParent();
            level++;
        }

        return level;
    }
}
