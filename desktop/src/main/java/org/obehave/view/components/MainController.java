package org.obehave.view.components;

import com.google.common.eventbus.Subscribe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.dialog.CommandLinksDialog;
import org.obehave.events.EventBusHolder;
import org.obehave.events.UiEvent;
import org.obehave.persistence.Daos;
import org.obehave.service.Study;
import org.obehave.util.FileUtil;
import org.obehave.util.I18n;
import org.obehave.util.properties.AppProperties;
import org.obehave.util.properties.AppPropertiesHolder;
import org.obehave.view.components.dialogs.AboutDialog;
import org.obehave.view.components.dialogs.ExportDialog;
import org.obehave.view.components.observation.ObservationControl;
import org.obehave.view.components.tree.ProjectTreeControl;
import org.obehave.view.util.AlertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController {
    private static final AppProperties PROPERTIES = AppPropertiesHolder.get();
    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private Study study;

    private AboutDialog aboutDialog;
    private ExportDialog exportDialog;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ProjectTreeControl tree;

    @FXML
    private SplitPane splitpane;

    @FXML
    private VBox vbox;

    @FXML
    private MenuBar menubar;

    private ObservationControl observationControl;

    private Stage stage;

    @FXML
    void close(ActionEvent event) {
        log.info("Closing application");

        Daos.closeAll();
        System.exit(0);
    }

    @FXML
    void initialize() {
        assert tree != null : "fx:id=\"tree\" was not injected: check your FXML file 'main.fxml'.";
        assert splitpane != null : "fx:id=\"splitpane\" was not injected: check your FXML file 'main.fxml'.";

        tree.setMinWidth(200);
        splitpane.prefHeightProperty().bind(vbox.heightProperty().subtract(menubar.heightProperty()));

        EventBusHolder.register(this);

        observationControl = new ObservationControl();
    }

    public void chooseStudy() {
        // TODO simplify! Maybe refactor into a seperate class.

        // Find out if creating or opening a new study
        final String createNewOne = I18n.get("ui.study.create.title");
        final String closeApplication = I18n.get("ui.study.close");

        do {
            // we can't reuse commandLinksDialog, because of a bug in it's implemenation.
            // if we would, selecting link 'n', aborting, selecting link 'n' again won't work.
            String selectedButtonTitle = getCommandLinksDialog().showAndWait().get().getText();

            if (selectedButtonTitle.equals(closeApplication)) {
                close(null);
            }
            // at this point, selectedButtonTitle can either be createNewOne or openExistingOne
            boolean create = selectedButtonTitle.equals(createNewOne);

            // configuring file chooser and show until a file was selected
            final File defaultSaveFolder = new File(PROPERTIES.saveFolder());

            FileChooser fileChooser = new FileChooser();
            if (defaultSaveFolder.exists()) {
                fileChooser.setInitialDirectory(defaultSaveFolder);
            } else {
                if (defaultSaveFolder.mkdirs()) {
                    fileChooser.setInitialDirectory(defaultSaveFolder);
                } else {
                    AlertUtil.showError(I18n.get("ui.study.error.defaultsavefolder.title"),
                            I18n.get("ui.study.error.defaultsavefolder.description", defaultSaveFolder));
                }
            }
            fileChooser.setTitle(selectedButtonTitle);
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Save file", "*" + PROPERTIES.databaseFileSuffix()));


            File chosenFile = create ? fileChooser.showSaveDialog(stage) : fileChooser.showOpenDialog(stage);
            if (chosenFile != null) {
                try {
                    if (FileUtil.isDatabaseFileLocked(chosenFile)) {
                        AlertUtil.showError("Database error", "This database is currently locked by another instance of obehave");
                        continue;
                    }

                    if (create) {
                        Optional<String> name;
                        do {
                            name = showStudyNameDialog(
                                    FileUtil.removeSuffixFromFileNameIfThere(chosenFile, PROPERTIES.databaseFileSuffix()));
                        } while (name.isPresent() && name.get().isEmpty());

                        if (name.isPresent()) {
                            study = Study.create(chosenFile);
                            study.setName(name.get());
                        }
                    } else {
                        try {
                            study = Study.load(chosenFile);
                        } catch (Exception e) {
                            AlertUtil.showError("Couldn't open database", "There was a problem loading the database.\n" +
                                    "Is your save file valid?", e);
                        }
                    }
                } catch (Exception e) {
                    AlertUtil.showError(I18n.get("ui.study.error.database.title"),
                            I18n.get("ui.study.error.database.description", e.getMessage()), e);
                }
            }
        } while (study == null);

        stage.setTitle(stage.getTitle() + " - " + study.getName() + " - " + study.getSavePath());

        tree.setStudy(study);
        observationControl.setStudy(study);
    }

    private CommandLinksDialog getCommandLinksDialog() {
        final String createNewOne = I18n.get("ui.study.create.title");
        final String openExistingOne = I18n.get("ui.study.open.title");
        final String closeApplication = I18n.get("ui.study.close");
        List<CommandLinksDialog.CommandLinksButtonType> links = Arrays.asList(
                new CommandLinksDialog.CommandLinksButtonType(closeApplication, false),
                new CommandLinksDialog.CommandLinksButtonType(createNewOne, I18n.get("ui.study.create.description"), false),
                new CommandLinksDialog.CommandLinksButtonType(openExistingOne, I18n.get("ui.study.open.description"), true));

        CommandLinksDialog commandLinksDialog = new CommandLinksDialog(links);
        commandLinksDialog.setTitle(I18n.get("ui.study.dialog"));

        return commandLinksDialog;
    }

    @FXML
    void addTreeComponent() {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private Optional<String> showStudyNameDialog(String preset) {
        return AlertUtil.askForString(I18n.get("ui.study.dialog.name.title"),
                I18n.get("ui.study.dialog.name.description"),
                preset);
    }

    @FXML
    void changeStudyName(ActionEvent event) {
        log.trace("Changing study name");

        Optional<String> name = showStudyNameDialog(study.getName());
        if (name.isPresent() && !name.get().isEmpty()) {
            study.setName(name.get());
        }
    }

    @FXML
    void settings(ActionEvent event) {
        log.trace("Showing settings");

        // TODO show settings
    }

    @FXML
    void manual(ActionEvent event) {
        log.trace("Showing manual");

        // TODO show manual
    }

    @FXML
    void export() {
        log.trace("Starting exporter");

        if (exportDialog == null) {
            exportDialog = new ExportDialog(stage);
        }

        exportDialog.showAndWait(study);
    }

    @FXML
    void about(ActionEvent event) {
        log.trace("Showing about popup");

        if (aboutDialog == null) {
            aboutDialog = new AboutDialog(stage);
        }

        aboutDialog.showAndWait();
    }

    @Subscribe
    public void loadObservation(UiEvent.LoadObservation event) {
        splitpane.getItems().set(1, observationControl);

        observationControl.loadObservation(event);
    }

}