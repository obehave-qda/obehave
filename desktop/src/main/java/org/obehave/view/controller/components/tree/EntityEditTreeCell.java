package org.obehave.view.controller.components.tree;

import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseEvent;
import org.obehave.model.Node;
import org.obehave.model.Observation;
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
        Object item = getItem().get();
        log.trace("Clicked tree item: {}", item);

        javafx.scene.Node ownerNode = getSkin().getNode();

        if (item instanceof Node) {
            Node node = (Node) item;

            // it's a node
            if (event.isShortcutDown() && node.getData() != null) {
                popOverHolder.get(node).show(ownerNode);
            } else if (event.getClickCount() >= 2 && node.getDataType() == Observation.class) {

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
