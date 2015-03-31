package org.obehave.view.components.tree.edit;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.joda.time.DateTime;
import org.obehave.model.Node;
import org.obehave.model.Observation;
import org.obehave.service.Study;
import org.obehave.view.util.AlertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;

/**
 * @author Markus Möslinger
 */
public class ObservationEditControl {
    private static final Logger log = LoggerFactory.getLogger(ObservationEditControl.class);

    private Node loadedObservationNode;

    private Runnable saveCallback;

    private Study study;

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

    public void selectVideo() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Video files", "*.mp4", "*.m4a", "*.m4v",
                "*.m3u8", "*.fxm", "*.flv"));
        setVideoPath(chooser.showOpenDialog(video.getScene().getWindow()));
    }

    public void setVideoPath(File path) {
        videoPath = path;

        if (videoPath != null) {
            video.setText(videoPath.getName());
        } else {
            video.setText("Select video...");
        }
    }

    public void loadObservation(Node node) {
        loadedObservationNode = node;
        Observation o = (Observation) node.getData();

        if (o == null) {
            setName("");
            setVideoPath(null);
        } else {
            setName(o.getName());
            setVideoPath(o.getVideo());
        }

        if (o != null && o.getDateTime() != null) {
            DateTime dt = o.getDateTime();
            date.setValue(LocalDate.of(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth()));
            hour.setText(String.format("%02d", dt.getHourOfDay()));
            minute.setText(String.format("%02d", dt.getMinuteOfHour()));
        } else {
            date.setValue(null);
            hour.setText("00");
            minute.setText("00");
        }

        name.requestFocus();
    }

    public void saveCurrent() {
        if (name.getText().isEmpty()) {
            AlertUtil.showError("Validation error", "Observation must have a name");
            return;
        }

        Observation o;

        if (loadedObservationNode.getData() == null) {
            log.debug("Creating new observation");
            o = new Observation();
        } else {
            log.debug("Saving existing observation");
            o = (Observation) loadedObservationNode.getData();
        }

        o.setName(getName());

        o.setVideo(videoPath);
        LocalDate pickedDate = date.getValue();

        if (pickedDate != null) {
            DateTime dt = new DateTime(pickedDate.getYear(), pickedDate.getMonthValue(), pickedDate.getDayOfMonth(),
                    Integer.valueOf(hour.getText()), Integer.valueOf(minute.getText()));

            o.setDateTime(dt);
        }

        study.getObservationService().save(o);
        if (loadedObservationNode.getData() == null) {
            loadedObservationNode.addChild(o);
        }
        study.getNodeService().save(loadedObservationNode);

        loadedObservationNode = null;
        saveCallback.run();
    }

    public void cancel() {
        saveCallback.run();
    }

    public void setSaveCallback(Runnable saveCallback) {
        this.saveCallback = saveCallback;
    }

    public void setStudy(Study study) {
        this.study = study;
    }
}
