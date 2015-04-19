package org.obehave.view.components.tree.edit;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.controlsfx.control.CheckListView;
import org.joda.time.DateTime;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Node;
import org.obehave.model.Observation;
import org.obehave.model.Subject;
import org.obehave.service.Study;
import org.obehave.util.DisplayWrapper;
import org.obehave.view.util.AlertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @FXML
    private ChoiceBox<DisplayWrapper<Subject>> focalSubject;

    @FXML
    private CheckListView<DisplayWrapper<Subject>> checkedSubjects;

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

        if (videoPath != null) {
            chooser.setInitialDirectory(videoPath.getParentFile());
        } else {
            chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        }

        setVideoPath(chooser.showOpenDialog(video.getScene().getWindow()));
    }

    public void setVideoPath(File path) {
        videoPath = path;

        if (videoPath != null) {
            video.setText(videoPath.getName());
            video.setTooltip(new Tooltip(videoPath.getAbsolutePath()));
            video.getTooltip().setStyle("-fx-background-color: white;");
        } else {
            video.setText("Select video...");
            video.setTooltip(null);
        }
    }

    public void loadObservation(Node node) {
        loadedObservationNode = node;
        Observation o = (Observation) node.getData();

        fillFocalSubjects();

        if (o == null) {
            setName("");
            setVideoPath(null);
            checkedSubjects.getCheckModel().clearChecks();
            focalSubject.getSelectionModel().select(null);
        } else {
            setName(o.getName());
            setVideoPath(o.getVideo());
            o.getParticipatingSubjects().forEach(s -> checkedSubjects.getCheckModel().check(DisplayWrapper.of(s)));
            focalSubject.getSelectionModel().select(DisplayWrapper.of(o.getFocalSubject()));
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

        o.setParticipatingSubjects(getCheckedSubjects());
        final DisplayWrapper<Subject> selectedFocalSubject = focalSubject.getSelectionModel().getSelectedItem();
        o.setFocalSubject(selectedFocalSubject != null ? selectedFocalSubject.get() : null);

        if (pickedDate != null) {
            DateTime dt = new DateTime(pickedDate.getYear(), pickedDate.getMonthValue(), pickedDate.getDayOfMonth(),
                    Integer.valueOf(hour.getText()), Integer.valueOf(minute.getText()));

            o.setDateTime(dt);
        }

        try {
            study.getObservationService().save(o);
            if (loadedObservationNode.getData() == null) {
                loadedObservationNode.addChild(o);
            }
            study.getNodeService().save(loadedObservationNode);

            loadedObservationNode = null;
            saveCallback.run();
        } catch (ServiceException exception) {
            AlertUtil.showError("Error", exception.getMessage(), exception);
        }
    }

    public void cancel() {
        saveCallback.run();
    }

    public void setSaveCallback(Runnable saveCallback) {
        this.saveCallback = saveCallback;
    }

    public void setStudy(Study study) {
        this.study = study;

        checkedSubjects.getItems().clear();
        study.getSubjectsList().forEach(s -> checkedSubjects.getItems().add(DisplayWrapper.of(s)));
    }

    private List<Subject> getCheckedSubjects() {
        final List<DisplayWrapper<Subject>> checkedItems = checkedSubjects.getCheckModel().getCheckedItems();
        List<Subject> subjects = new ArrayList<>(checkedItems.size());

        checkedItems.forEach(subjectDisplayWrapper -> subjects.add(subjectDisplayWrapper.get()));

        return subjects;
    }

    private void fillFocalSubjects() {
        List<DisplayWrapper<Subject>> subjects = new ArrayList<>();
        subjects.add(null);

        study.getSubjectsList().forEach(s -> subjects.add(DisplayWrapper.of(s)));

        focalSubject.getItems().clear();
        focalSubject.getItems().addAll(subjects);
    }
}
