package org.obehave.view.controller.components.tree;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import org.obehave.model.Action;
import org.obehave.model.Node;
import org.obehave.service.NodeService;
import org.obehave.util.DisplayWrapper;
import org.obehave.view.util.TreeUtil;

import java.util.function.Supplier;

/**
 * @author Markus Möslinger
 */
public class ContextMenuBuilder {
    private final PopOverHolder popOverHolder;
    private Node node;
    private int hierarchyLevel;
    private Supplier<javafx.scene.Node> ownerNodeSupplier;
    private NodeService nodeService = NodeService.getInstance();

    private ContextMenu cm = new ContextMenu();

    private ContextMenuBuilder(PopOverHolder popOverHolder) {
        this.popOverHolder = popOverHolder;
    }

    public static ContextMenu forItem(PopOverHolder popOverHolder, TreeItem<DisplayWrapper<?>> treeItem,
                                      Supplier<javafx.scene.Node> ownerNodeSupplier) {
        ContextMenuBuilder builder = new ContextMenuBuilder(popOverHolder);

        builder.node = (Node) treeItem.getValue().get();
        builder.hierarchyLevel = TreeUtil.getHierarchyLevel(treeItem);
        builder.ownerNodeSupplier = ownerNodeSupplier;

        builder.addNewGroup();
        builder.addNewItem();
        builder.addEditGroup();
        builder.addEditItem();
        builder.addDeleteItem();

        return builder.cm;
    }

    private void addNewGroup() {
        if (node.getData() == null && node.getDataType() == Action.class && hierarchyLevel == 1) {
            final MenuItem menuItem = new MenuItem("Add group");
            menuItem.setOnAction(event -> popOverHolder.get(node).show(ownerNodeSupplier.get()));

            cm.getItems().add(menuItem);
        }
    }

    private void addNewItem() {
        if (node.getData() == null && (hierarchyLevel == 1 || (node.getDataType() == Action.class && hierarchyLevel == 2))) {
            final MenuItem menuItem = new MenuItem("Add item");
            menuItem.setOnAction(event -> popOverHolder.get(node).show(ownerNodeSupplier.get()));

            cm.getItems().add(menuItem);
        }
    }

    private void addEditGroup() {
        if (node.getData() == null && node.getDataType() == Action.class && hierarchyLevel == 2) {
            final MenuItem menuItem = new MenuItem("Edit group");
            menuItem.setOnAction(event -> popOverHolder.get(node).show(ownerNodeSupplier.get()));

            cm.getItems().add(menuItem);
        }
    }

    private void addEditItem() {
        if (node.getData() != null) {
            final MenuItem menuItem = new MenuItem("Edit item");
            menuItem.setOnAction(event -> popOverHolder.get(node).show(ownerNodeSupplier.get()));

            cm.getItems().add(menuItem);
        }
    }

    private void addDeleteItem() {
        if (node.getData() != null) {
            final MenuItem menuItem = new MenuItem("Delete item");
            menuItem.setOnAction(event -> {
                node.getParent().remove(node);
                nodeService.save(node.getParent());
                nodeService.delete(node);
            });

            cm.getItems().add(menuItem);
        }
    }
}
