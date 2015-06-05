package org.obehave.view.components.dialogs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.obehave.view.util.HostServicesHolder;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Dialog to show information about and version of obehave
 */
public class AboutDialog extends Stage implements Initializable {
    @FXML
    private Label versionLabel;

    public AboutDialog(Window owner) {
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UTILITY);
        setResizable(false);

        setTitle("About obehave");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("org/obehave/view/components/dialogs/about.fxml"));
        fxmlLoader.setController(this);

        try {
            Pane root = fxmlLoader.load();

            setScene(new Scene(root));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


    @FXML
    public void handleClose() {
        close();
    }

    @FXML
    public void showWebsite() {
        HostServicesHolder.get().showDocument("http://www.obehave.org");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final String version = getClass().getPackage().getImplementationVersion();

        if (version != null) {
            versionLabel.setText("Version: " + version);
        } else {
            versionLabel.setText("Couldn't determine version. Started from an IDE?");
        }
    }
}
