package org.obehave.view.controller.components.tree;

import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseEvent;
import org.obehave.model.Action;
import org.obehave.model.Node;
import org.obehave.model.Observation;
import org.obehave.model.Subject;
import org.obehave.util.DisplayWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Markus Möslinger
 */
public class EntityEditTreeCell extends TextFieldTreeCell<DisplayWrapper<?>> {
    private static final Logger log = LoggerFactory.getLogger(EntityEditTreeCell.class);

    public EntityEditTreeCell() {

        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handle());
    }

    private void handle() {
        Object node = getItem().get();
        log.trace("Clicked on {}", node);

        javafx.scene.Node ownerNode = getSkin().getNode();

        if (node instanceof Node) {
            Object item = ((Node) node).getData();
            log.trace("It's a node with {}", item);

            if (item != null) {
                if (item instanceof Subject) {
                    handleSubject((Subject) item, ownerNode);
                } else if (item instanceof Action) {
                    handleAction((Action) item, ownerNode);
                } else if (item instanceof Observation) {
                    handleObservation((Observation) item, ownerNode);
                }
            }
        }
    }

    private void handleSubject(Subject subject, javafx.scene.Node owner) {
        PopOverHolder.hideAllAndGetSubject(subject).show(owner);
    }

    private void handleAction(Action action, javafx.scene.Node owner) {
        PopOverHolder.hideAllAndGetAction(action).show(owner);
    }

    private void handleObservation(Observation observation, javafx.scene.Node owner) {
        PopOverHolder.hideAllAndGetObservation(observation).show(owner);
    }
}
