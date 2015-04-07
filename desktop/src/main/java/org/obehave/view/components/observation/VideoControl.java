package org.obehave.view.components.observation;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
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
    private final DoubleProperty msPlayed = new SimpleDoubleProperty(this, "msPlayed", 0);
    private final DoubleProperty codingHeight = new SimpleDoubleProperty(this, "codingHeight", 0);

    private ChangeListener<Duration> currentTimeListener = (observable, oldValue, newValue) -> msPlayed.setValue(newValue.toMillis());

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

        mediaView.setPreserveRatio(true);
    }

    @FXML
    void playPause(ActionEvent event) {
        if (mediaView.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
            log.trace("Pausing video at {}s", mediaView.getMediaPlayer().getCurrentTime().toSeconds());
            mediaView.getMediaPlayer().pause();
        } else {
            log.trace("Playing video at {}s", mediaView.getMediaPlayer().getCurrentTime().toSeconds());
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

        if (mediaView.getMediaPlayer() != null) {
            mediaView.getMediaPlayer().currentTimeProperty().removeListener(currentTimeListener);
            mediaView.getMediaPlayer().dispose();
        }

        mediaView.setMediaPlayer(new MediaPlayer(media));

        //TODO: subtract width until splitpane for tree is fixed

        mediaView.fitWidthProperty().bind(mediaView.getScene().widthProperty().subtract(200));
        mediaView.fitHeightProperty().bind(mediaView.getScene().heightProperty()
                .subtract(codingHeight).subtract(200));


        mediaView.getMediaPlayer().currentTimeProperty().addListener(currentTimeListener);
        mediaView.getMediaPlayer().play();
    }

    public DoubleProperty msPlayed() {
        return msPlayed;
    }

    public ReadOnlyObjectProperty<Duration> totalDurationProperty() {
        return mediaView.getMediaPlayer().totalDurationProperty();
    }

    public DoubleProperty codingHeight() {
        return codingHeight;
    }
}
