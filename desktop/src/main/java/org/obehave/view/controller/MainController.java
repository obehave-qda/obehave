package org.obehave.view.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.dialog.CommandLinksDialog;
import org.obehave.model.Study;
import org.obehave.util.I18n;
import org.obehave.util.Property;
import org.obehave.view.controller.components.VideoComponent;
import org.obehave.view.controller.components.coding.CodingComponent;
import org.obehave.view.controller.components.tree.ProjectTreeComponent;
import org.obehave.view.util.AlertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private Study study;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ProjectTreeComponent tree;

    @FXML
    private VideoComponent videoComponent;

    @FXML
    private SplitPane splitpane;

    @FXML
    private VBox vbox;

    @FXML
    private MenuBar menubar;

    @FXML
    private CodingComponent codingComponent;

    @FXML
    private BorderPane contentBorderPane;

    private Stage stage;

    @FXML
    void loadVideo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(I18n.getString("ui.video.open.title"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(I18n.getString("ui.filefilter.video"), "*.mp4"),
                new FileChooser.ExtensionFilter(I18n.getString("ui.filefilter.all"), "*.*"));

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
        assert splitpane != null : "fx:id=\"splitpane\" was not injected: check your FXML file 'main.fxml'.";

        splitpane.prefHeightProperty().bind(vbox.heightProperty().subtract(menubar.heightProperty()));

        videoComponent.maxHeightProperty().bind(contentBorderPane.heightProperty().divide(1.5));
        codingComponent.maxHeightProperty().bind(contentBorderPane.heightProperty().divide(3));
    }

    public void chooseStudy() {
        // Find out if creating or opening a new study
        final String createNewOne = I18n.getString("ui.study.create.title");
        final String openExistingOne = I18n.getString("ui.study.open.title");
        final String closeApplication = I18n.getString("ui.study.close");
        List<CommandLinksDialog.CommandLinksButtonType> links = Arrays.asList(
                new CommandLinksDialog.CommandLinksButtonType(createNewOne, I18n.getString("ui.study.create.description"), false),
                new CommandLinksDialog.CommandLinksButtonType(openExistingOne, I18n.getString("ui.study.open.description"), false),
                new CommandLinksDialog.CommandLinksButtonType(closeApplication, false));

        CommandLinksDialog commandLinksDialog = new CommandLinksDialog(links);
        commandLinksDialog.setTitle(I18n.getString("ui.study.dialog"));

        File chosenFile;
        boolean create;

        do {
            String selectedButtonTitle = commandLinksDialog.showAndWait().get().getText();

            if (selectedButtonTitle.equals(closeApplication)) {
                close(null);
            }
            // at this point, selectedButtonTitle can either be createNewOne or openExistingOne
            create = selectedButtonTitle.equals(createNewOne);

            // configuring file chooser and show until a file was selected
            final File defaultSaveFolder = Property.getSaveFolder();

            FileChooser fileChooser = new FileChooser();
            if (!defaultSaveFolder.exists()) {
                if (defaultSaveFolder.mkdirs()) {
                    fileChooser.setInitialDirectory(defaultSaveFolder);
                } else {
                    AlertUtil.showError(I18n.getString("ui.study.error.defaultsavefolder.title"),
                            I18n.getString("ui.study.error.defaultsavefolder.description", defaultSaveFolder));
                }
            }
            fileChooser.setTitle(selectedButtonTitle);


            chosenFile = create ? fileChooser.showSaveDialog(stage) : fileChooser.showOpenDialog(stage);
        } while (chosenFile == null);

        study = create ? Study.create(chosenFile) : Study.load(chosenFile);
        study.setName("Obehave Study Test");

        stage.setTitle(stage.getTitle() + ": " + chosenFile.getAbsolutePath());
        tree.setStudy(study);
    }

    @FXML
    void addTreeComponent() {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}