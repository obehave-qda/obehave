package org.obehave.view.components.dialogs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.stage.*;
import javafx.stage.Window;
import org.controlsfx.control.CheckListView;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.obehave.model.Node;
import org.obehave.model.Observation;
import org.obehave.model.Subject;
import org.obehave.service.ExcelWriter;
import org.obehave.service.Exporter;
import org.obehave.service.MatrixExporter;
import org.obehave.service.Study;
import org.obehave.util.DisplayWrapper;
import org.obehave.view.util.AlertUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ExportDialog extends Stage {
    private File path;

    @FXML
    private Label labelPath;
    @FXML
    private CheckListView<DisplayWrapper<Observation>> observations;
    @FXML
    private CheckListView<DisplayWrapper<Subject>> subjects;
    @FXML
    private TreeView<DisplayWrapper<Node>> actions;

    public ExportDialog(Window owner) {
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UTILITY);
        setResizable(false);

        setTitle("Exporter");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("org/obehave/view/components/dialogs/export.fxml"));
        fxmlLoader.setController(this);

        try {
            Pane root = fxmlLoader.load();

            setScene(new Scene(root));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void showAndWait(Study study) {
        subjects.getItems().clear();
        observations.getItems().clear();
        study.getSubjectsList().forEach(s -> subjects.getItems().add(DisplayWrapper.of(s)));
        study.getObservationsList().forEach(s -> observations.getItems().add(DisplayWrapper.of(s)));

        final TreeItem<DisplayWrapper<Node>> root = createTreeItem(study.getActions());
        root.setExpanded(true);
        actions.setRoot(root);

        showAndWait();
    }

    private TreeItem<DisplayWrapper<Node>> createTreeItem(Node node) {
        TreeItem<DisplayWrapper<Node>> treeItem = new TreeItem<>();
        treeItem.setExpanded(true);
        treeItem.setValue(DisplayWrapper.of(node));

        List<Node> children = node.getChildren();

        for (Node child : children) {
            treeItem.getChildren().add(createTreeItem(child));
        }

        return treeItem;
    }

    @FXML
    public void cancel() {
        close();
    }

    @FXML
    public void chooseDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose folder to export to");
        final File defaultDir = new File(System.getProperty("user.home") + "/obehave/exports");
        if (!defaultDir.exists()) {
            if (!defaultDir.mkdirs()) {
                AlertUtil.showError("Error creating default exports directory", "Couldn't create directory at "
                        + defaultDir.getAbsolutePath());
            }
        }

        if (path != null || !defaultDir.exists()) {
            chooser.setInitialDirectory(path);
        } else {
            chooser.setInitialDirectory(defaultDir);
        }


        path = chooser.showDialog(labelPath.getScene().getWindow());

        if (path != null) {
            labelPath.setText(path.getAbsolutePath());
        } else {
            labelPath.setText("Choose export directory...");
        }
    }

    @FXML
    public void export() {
        if (path == null) {
            AlertUtil.showError("Input Error", "You have to select a path to export to!");
            return;
        }

        List<Observation> selectedObservations = getObservationsFromChecklist();
        if (selectedObservations.isEmpty()) {
            AlertUtil.showError("Input Error", "You have to select at least one observation!");
            return;
        }

        List<Subject> selectedSubjects = getSubjectsFromChecklist();
        if (selectedSubjects.isEmpty()) {
            AlertUtil.showError("Input Error", "You have to select at least one subject!");
            return;
        }

        Node actionNode = getActionFromTree();
        if (actionNode == null) {
            AlertUtil.showError("Input Error", "You have to select a single action or a group of actions!");
            return;
        }

        final Exporter exporter = new MatrixExporter(new ExcelWriter(path), true);
        try {
            exporter.export(selectedObservations, selectedSubjects, Collections.singletonList(actionNode));

            if (Desktop.isDesktopSupported()) {
                final org.controlsfx.control.action.Action response =
                        AlertUtil.showConfirm("Export finished",
                                "Exporting data has finished. Do you want to open the export folder?",
                                labelPath.getScene().getWindow());

                if (response == Dialog.ACTION_YES) {
                    Desktop.getDesktop().open(path);
                }
            } else {
                Dialogs.create().title("Success").message("Successfully exported into " + path.getAbsolutePath()).showInformation();
            }

            close();
        } catch (Exception e) {
            AlertUtil.showError("Error during export", e.getMessage(), e);
        }
    }

    private List<Observation> getObservationsFromChecklist() {
        return observations.getCheckModel().getCheckedItems().stream().map(DisplayWrapper::get).collect(Collectors.toList());
    }

    private List<Subject> getSubjectsFromChecklist() {
        return subjects.getCheckModel().getCheckedItems().stream().map(DisplayWrapper::get).collect(Collectors.toList());
    }

    private Node getActionFromTree() {
        final TreeItem<DisplayWrapper<Node>> treeItem = actions.getSelectionModel().getSelectedItem();

        return treeItem == null ? null : treeItem.getValue().get();
    }
}
