package org.obehave.view.controller.components.coding;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import org.obehave.model.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The SubjectsList draws every Subject's title on the left screen side
 */
public class SubjectsList extends Pane {
    private static Logger log = LoggerFactory.getLogger(SubjectsList.class);

    private List<Subject> subjects = new ArrayList<>();
    private DoubleProperty subjectHeightProperty = new SimpleDoubleProperty(this, "subjectHeightProperty");

    public SubjectsList() {
        setPrefWidth(100);
    }

    public DoubleProperty subjectHeightProperty() {
        return subjectHeightProperty;
    }

    public void refresh() {
        getChildren().clear();
        for (int i = 0; i < subjects.size(); i++) {
            final Subject subject = subjects.get(i);

            Text subjectText = new Text(subject.getDisplayString());
            subjectText.xProperty().setValue(2);
            subjectText.yProperty().bind(subjectHeightProperty.multiply(i).add(subjectHeightProperty.divide(2)));
            subjectText.setTranslateY(-(subjectText.getBoundsInParent().getHeight() / 2));
            subjectText.getStyleClass().add("secondLabel");
            subjectText.setFontSmoothingType(FontSmoothingType.LCD);
            getChildren().add(subjectText);


            log.trace("Added label subject {}: {}", subject, subjectText);
        }
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
        return;
    }
}
