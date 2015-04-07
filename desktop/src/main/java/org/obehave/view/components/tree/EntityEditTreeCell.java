package org.obehave.view.components.tree;

import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseEvent;
import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
import org.obehave.model.Node;
import org.obehave.model.Observation;
import org.obehave.service.Study;
import org.obehave.util.DisplayWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Markus Möslinger
 */
public class EntityEditTreeCell extends TextFieldTreeCell<DisplayWrapper<?>> {
    private static final Logger log = LoggerFactory.getLogger(EntityEditTreeCell.class);

    private final Study study;
    private final PopOverHolder popOverHolder;

    public EntityEditTreeCell(Study study, PopOverHolder popOverHolder) {
        this.study = study;
        this.popOverHolder = popOverHolder;

        addEventHandler(MouseEvent.MOUSE_CLICKED, this::handle);
    }

    private void handle(MouseEvent event) {
        if (getItem() != null) {
            Object item = getItem().get();
            log.trace("Clicked tree item: {}", item);

            javafx.scene.Node ownerNode = getSkin().getNode();

            if (item instanceof Node) {
                Node node = (Node) item;

                // it's a node
                if (event.isShortcutDown() && node.getData() != null) {
                    popOverHolder.get(node).show(ownerNode);
                } else if (event.getClickCount() >= 2 && node.getData() != null && node.getDataType() == Observation.class) {
                    EventBusHolder.post(new UiEvent.LoadObservation((Observation) node.getData()));
                }
            }
        }
    }

    @Override
    public void updateItem(DisplayWrapper<?> item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && getTreeItem().getParent() != null) {
            setContextMenu(ContextMenuBuilder.forItem(study, popOverHolder, getTreeItem(), this::getNode));
        }
    }

    private javafx.scene.Node getNode() {
        return getSkin().getNode();
    }
}
