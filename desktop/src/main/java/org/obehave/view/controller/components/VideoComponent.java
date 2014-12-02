package org.obehave.view.controller.components;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class VideoComponent extends BorderPane {
    private static final Logger log = LoggerFactory.getLogger(VideoComponent.class);

    @FXML
    private MediaView mediaView;

    public VideoComponent() {
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ui/components/videoComponent.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    void playPause(ActionEvent event) {
        if (mediaView.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
            log.trace("Button clicked - pausing video");
            mediaView.getMediaPlayer().pause();
        } else {
            log.trace("Button clicked - playing video");
            mediaView.getMediaPlayer().play();
        }
    }

    private Media toMedia(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }

        log.debug("Converting {} to Media", file);
        return new Media(file.toURI().toString());
    }

    public void loadFile(File file) {
        Media media = toMedia(file);

        mediaView.setMediaPlayer(new MediaPlayer(media));

        mediaView.getMediaPlayer().play();
    }
}
