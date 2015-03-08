package org.obehave.view.controller.components.edit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.joda.time.DateTime;
import org.obehave.model.Observation;
import org.obehave.service.ObservationService;
import org.obehave.view.Obehave;
import org.obehave.view.controller.components.tree.PopOverHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;

/**
 * @author Markus Möslinger
 */
public class ObservationEditController {
    private static final Logger log = LoggerFactory.getLogger(ObservationEditController.class);
    private static final ObservationService observationService = ObservationService.getInstance();

    private Observation loadedObservation;

    @FXML
    private TextField name;

    @FXML
    private Button video;

    private File videoPath;

    @FXML
    private DatePicker date;

    @FXML
    private TextField hour;
    @FXML
    private TextField minute;

    public String getName() {
        return name.getText();
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void selectVideo(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        videoPath = chooser.showOpenDialog(Obehave.STAGE);

        if (videoPath != null) {
            video.setText(videoPath.getName());
        }
    }

    public void loadObservation(Observation o) {
        loadedObservation = o;

        setName(o.getName());
        DateTime dt = o.getDateTime();
        date.setValue(LocalDate.of(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth()));
        hour.setText(String.format("%2d", dt.getHourOfDay()));
        minute.setText(String.format("%2d", dt.getMinuteOfHour()));
    }

    public void saveCurrent(ActionEvent e) {
        if (loadedObservation == null) {
            log.debug("Creating new observation");
            loadedObservation = new Observation(getName());
        } else {
            log.debug("Saving existing observation");
            loadedObservation.setName(getName());
        }

        loadedObservation.setVideo(videoPath);
        LocalDate pickedDate = date.getValue();

        DateTime dt = new DateTime(pickedDate.getYear(), pickedDate.getMonthValue(), pickedDate.getDayOfMonth(),
                Integer.valueOf(hour.getText()), Integer.valueOf(minute.getText()));

        loadedObservation.setDateTime(dt);

        observationService.save(loadedObservation);

        loadedObservation = null;
        PopOverHolder.hidePopOvers();
    }
}
