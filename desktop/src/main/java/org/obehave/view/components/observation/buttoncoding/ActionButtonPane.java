package org.obehave.view.components.observation.buttoncoding;

import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Action;
import org.obehave.service.Study;

/**
 * Created by Markus.Moeslinger on 18.06.2015.
 */
public class ActionButtonPane extends TilePane {
    public ActionButtonPane() {
        EventBusHolder.register(this);
        setPrefColumns(2);
    }

    public void setStudy(Study study) {
        getChildren().clear();
        study.getActionList().forEach(this::addAction);
    }

    public void addAction(Action action) {
        final Button actionButton = new ActionButton(action);

        if (!getChildren().contains(actionButton)) {
            getChildren().add(actionButton);
        }
    }

    private static class ActionButton extends Button {
        private final Action action;

        public ActionButton(Action action) {
            super(action.getModifierFactory() == null ? action.getDisplayString() : action.getDisplayString() + " >");

            this.action = action;

            setOnAction(event -> EventBusHolder.post(new Eventsis.ActionClicked(action)));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            ActionButton that = (ActionButton) o;

            return new EqualsBuilder()
                    .append(action, that.action)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(action)
                    .toHashCode();
        }
    }
}
