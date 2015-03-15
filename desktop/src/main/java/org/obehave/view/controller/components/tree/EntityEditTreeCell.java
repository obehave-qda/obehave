package org.obehave.view.controller.components.tree;

import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseEvent;
import org.obehave.model.Node;
import org.obehave.util.DisplayWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Markus Möslinger
 */
public class EntityEditTreeCell extends TextFieldTreeCell<DisplayWrapper<?>> {
    private static final Logger log = LoggerFactory.getLogger(EntityEditTreeCell.class);

    private PopOverHolder popOverHolder;

    public EntityEditTreeCell(PopOverHolder popOverHolder) {
        this.popOverHolder = popOverHolder;

        addEventHandler(MouseEvent.MOUSE_CLICKED, this::handle);
    }

    private void handle(MouseEvent event) {
        Object node = getItem().get();
        log.trace("Clicked on {}", node);

        javafx.scene.Node ownerNode = getSkin().getNode();

        if (node instanceof Node) {
            Object item = ((Node) node).getData();
            log.trace("It's a node with {}", item);

            // it's a node
            if (event.isShortcutDown() && ((Node) node).getData() != null) {
                popOverHolder.get((Node) node).show(ownerNode);
            }
        }
    }

    @Override
    public void updateItem(DisplayWrapper<?> item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && getTreeItem().getParent() != null) {
            setContextMenu(ContextMenuBuilder.forItem(popOverHolder, getTreeItem(), this::getNode));
        }
    }

    private javafx.scene.Node getNode() {
        return getSkin().getNode();
    }
}
