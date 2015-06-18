package org.obehave.view.components.observation.buttoncoding;

import com.google.common.eventbus.Subscribe;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Circle;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
import org.obehave.model.Subject;
import org.obehave.view.util.ColorConverter;

/**
 * Created by Markus.Moeslinger on 18.06.2015.
 */
public class SubjectButtonPane extends TilePane {
    public SubjectButtonPane() {
        EventBusHolder.register(this);
        setPrefColumns(2);
    }

    @Subscribe
    public void loadObservation(UiEvent.LoadObservation loadObservation) {
        getChildren().clear();

        loadObservation.getObservation().getParticipatingSubjects().forEach(this::addSubject);
    }

    public void addSubject(Subject subject) {
        final Button subjectButton = new SubjectButton(subject);

        if (!getChildren().contains(subjectButton)) {
            getChildren().add(subjectButton);
        }
    }

    private static class SubjectButton extends Button {
        private final Subject subject;

        public SubjectButton(Subject subject) {
            super(subject.getDisplayString() + " >", new Circle(5, ColorConverter.convertToJavaFX(subject.getColor())));

            this.subject = subject;

            setOnAction(event -> EventBusHolder.post(new eventsi.SubjectClicked(subject)));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            SubjectButton that = (SubjectButton) o;

            return new EqualsBuilder()
                    .append(subject, that.subject)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(subject)
                    .toHashCode();
        }
    }
}
