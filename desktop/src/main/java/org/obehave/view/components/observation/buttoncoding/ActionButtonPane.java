package org.obehave.view.components.observation.buttoncoding;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Action;
import org.obehave.service.Study;

/**
 * Created by Markus.Moeslinger on 18.06.2015.
 */
public class ActionButtonPane extends BorderPane {
    private final TilePane tilePane = new TilePane();

    public ActionButtonPane() {
        EventBusHolder.register(this);

        tilePane.setPrefColumns(2);

        setTop(new Text("Choose action"));
        setCenter(tilePane);
    }

    public void setStudy(Study study) {
        tilePane.getChildren().clear();
        study.getActionList().forEach(this::addAction);
    }

    public void addAction(Action action) {
        final Button actionButton = new ActionButton(action);

        if (!tilePane.getChildren().contains(actionButton)) {
            tilePane.getChildren().add(actionButton);
        }
    }

    private static class ActionButton extends Button {
        private final Action action;

        public ActionButton(Action action) {
            super(action.getModifierFactory() == null ? action.getDisplayString() : action.getDisplayString() + " >");

            this.action = action;

            setOnAction(event -> EventBusHolder.post(new Events.ActionClicked(action)));
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
