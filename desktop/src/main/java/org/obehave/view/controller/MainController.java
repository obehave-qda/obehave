package org.obehave.view.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.obehave.model.Study;
import org.obehave.view.controller.components.tree.ProjectTreeComponent;
import org.obehave.view.controller.components.VideoComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Markus.Moeslinger on 02.12.2014.
 */
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private Study study = new Study("Whatever");

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ProjectTreeComponent tree;

    @FXML
    private VideoComponent videoComponent;

    @FXML
    void loadVideo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Media File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Media Files", "*.mp4"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        videoComponent.loadFile(fileChooser.showOpenDialog(null));
    }

    @FXML
    void close(ActionEvent event) {
        log.info("Closing application");
        ((Stage) videoComponent.getScene().getWindow()).close();
    }

    @FXML
    void initialize() {
        assert tree != null : "fx:id=\"tree\" was not injected: check your FXML file 'main.fxml'.";
        assert videoComponent != null : "fx:id=\"videoComponent\" was not injected: check your FXML file 'main.fxml'.";

        tree.setStudy(study);
    }

    @FXML
    void addTreeComponent() {

    }
}
