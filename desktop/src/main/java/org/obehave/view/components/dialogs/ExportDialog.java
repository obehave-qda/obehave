package org.obehave.view.components.dialogs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.stage.*;
import org.controlsfx.control.CheckListView;
import org.controlsfx.dialog.Dialog;
import org.obehave.model.Action;
import org.obehave.model.Node;
import org.obehave.model.Observation;
import org.obehave.model.Subject;
import org.obehave.service.ExcelExporter;
import org.obehave.service.Study;
import org.obehave.util.DisplayWrapper;
import org.obehave.view.util.AlertUtil;
import org.obehave.view.util.HostServicesHolder;

import java.io.File;
import java.io.IOException;
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
    public void chooseFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose folder to export to");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel files", ".xlsx"));

        path = chooser.showOpenDialog(labelPath.getScene().getWindow());

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

        final ExcelExporter exporter = new ExcelExporter(path);
        try {
            if (actionNode.getData() == null) {
                exporter.exportActionGroup(selectedObservations, selectedSubjects, actionNode);
            } else {
                exporter.exportAction(selectedObservations, selectedSubjects, (Action) actionNode.getData());
            }

            final org.controlsfx.control.action.Action response =
                    AlertUtil.showConfirm("Export finished", "Exporting data has finished. Do you want to open the export?",
                            labelPath.getScene().getWindow());

            if (response == Dialog.ACTION_YES) {
                // not sure if HostServicesHolder is able to open excel files. Let's try.
                HostServicesHolder.get().showDocument(path.toURI().toString());
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
