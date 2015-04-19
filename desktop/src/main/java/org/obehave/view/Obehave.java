package org.obehave.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.obehave.events.EventBusHolder;
import org.obehave.util.I18n;
import org.obehave.view.components.MainController;
import org.obehave.view.util.HostServicesHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.net.URL;

public class Obehave extends Application {
    private static final Logger log = LoggerFactory.getLogger(Obehave.class);

    public static Stage STAGE;

    @Override
    public void start(Stage stage) throws Exception {
        final String implementationVersion = getClass().getPackage().getImplementationVersion();
        if (implementationVersion != null) {
            log.info("Starting obehave v{}", implementationVersion);
        } else {
            log.warn("Couldn't determine executed version of obehave. That could be because you started it from an IDE");
        }

        final URL mainFxml = getClass().getClassLoader().getResource("org/obehave/view/components/main.fxml");
        if (mainFxml == null) {
            throw new IllegalStateException("Couldn't load main.fxml!");
        }

        FXMLLoader loader = new FXMLLoader(mainFxml);
        loader.setResources(I18n.bundle());
        Parent root = loader.load();
        final MainController mainController = loader.getController();
        mainController.setStage(stage);
        STAGE = stage;

        Scene scene = new Scene(root);

        stage.setTitle("obehave");
        stage.setScene(scene);
        stage.setMaximized(true);

        HostServicesHolder.initialize(getHostServices());

        stage.getIcons().addAll(new Image(getClass().getClassLoader().getResourceAsStream("org/obehave/view/icons/icon_16x16.png")),
                new Image(getClass().getClassLoader().getResourceAsStream("org/obehave/view/icons/icon_32x32.png")),
                new Image(getClass().getClassLoader().getResourceAsStream("org/obehave/view/icons/icon_48x48.png")),
                new Image(getClass().getClassLoader().getResourceAsStream("org/obehave/view/icons/icon_96x96.png")),
                new Image(getClass().getClassLoader().getResourceAsStream("org/obehave/view/icons/icon_144x144.png")),
                new Image(getClass().getClassLoader().getResourceAsStream("org/obehave/view/icons/icon_256x256.png")));

        EventBusHolder.register(mainController);
        mainController.chooseStudy();
        stage.show();
    }

    public static void main(String[] args) {
        SLF4JBridgeHandler.install();

        launch(args);
    }
}
