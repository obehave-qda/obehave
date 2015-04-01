package org.obehave.view.components.observation;


import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.obehave.exceptions.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class VideoControl extends BorderPane {
    private static final Logger log = LoggerFactory.getLogger(VideoControl.class);

    @FXML
    private MediaView mediaView;

    public VideoControl() {
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("org/obehave/view/components/observation/videoControl.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        mediaView.fitWidthProperty().bind(prefWidthProperty());
        mediaView.fitHeightProperty().bind(prefHeightProperty().subtract(30));
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

    @FXML
    void slower(ActionEvent event) {
        final double oldRate = mediaView.getMediaPlayer().getRate();
        final double newRate = oldRate / 2;

        log.trace("Slowing playback down from {} to {}", oldRate, newRate);
        mediaView.getMediaPlayer().setRate(newRate);
    }

    @FXML
    void faster(ActionEvent faster) {
        final double oldRate = mediaView.getMediaPlayer().getRate();
        final double newRate = oldRate * 2;

        log.trace("Speeding playback up from {} to {}", oldRate, newRate);
        mediaView.getMediaPlayer().setRate(newRate);
    }

    private Media toMedia(File file) {
        Validate.isNotNull(file, "File");

        return new Media(file.toURI().toString());
    }

    public void loadVideo(File file) {
        Media media = toMedia(file);

        mediaView.setMediaPlayer(new MediaPlayer(media));

        mediaView.getMediaPlayer().play();
    }

    public ReadOnlyObjectProperty<Duration> currentTime() {
        return mediaView.getMediaPlayer().currentTimeProperty();
    }
}
