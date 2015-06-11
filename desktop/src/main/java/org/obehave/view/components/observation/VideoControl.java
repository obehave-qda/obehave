package org.obehave.view.components.observation;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.obehave.exceptions.Validate;
import org.obehave.view.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.controlsfx.glyphfont.FontAwesome.Glyph.*;

public class VideoControl extends BorderPane implements Initializable{
    private static final Logger log = LoggerFactory.getLogger(VideoControl.class);
    private final DoubleProperty msPlayed = new SimpleDoubleProperty(this, "msPlayed", 0);
    private final DoubleProperty codingHeight = new SimpleDoubleProperty(this, "codingHeight", 0);

    private ChangeListener<Duration> currentTimeListener = (observable, oldValue, newValue) -> msPlayed.setValue(newValue.toMillis());

    private GlyphFont fontAwesome = new FontAwesome(getClass().getClassLoader()
            .getResourceAsStream("/org/obehave/view/font/awesome-4.3.0/FontAwesome.otf"));

    @FXML
    private MediaView mediaView;

    @FXML
    private Button mute;
    @FXML
    private Button slower;
    @FXML
    private Button faster;
    @FXML
    private Button playpause;

    private boolean playing = true;

    private StopWatch stopWatch = new StopWatch();

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
    void playPause() {
        if (playing) {
            pause();
        } else {
            start();
        }
    }

    public void pause() {
        log.trace("Pausing at {}s", mediaView.getMediaPlayer().getCurrentTime().toSeconds());
        mediaView.getMediaPlayer().pause();
        stopWatch.stop();
        playpause.setGraphic(fontAwesome.create(PLAY));

        // keep them synchronized
        mediaView.getMediaPlayer().seek(Duration.millis(stopWatch.getElapsedTime()));

        playing = false;
    }

    public void start() {
        log.trace("Playing at {}s", mediaView.getMediaPlayer().getCurrentTime().toSeconds());
        mediaView.getMediaPlayer().play();
        stopWatch.start();
        playpause.setGraphic(fontAwesome.create(PAUSE));

        playing = true;
    }

    @FXML
    void mute() {
        final double volume = mediaView.getMediaPlayer().getVolume();

        if (volume == 0) {
            log.trace("Enabling sound for video");
            mediaView.getMediaPlayer().setVolume(1);
            mute.setText("");
            mute.setGraphic(fontAwesome.create(VOLUME_OFF));
        } else {
            log.trace("Disabling sound for video");
            mediaView.getMediaPlayer().setVolume(0);
            mute.setText("");
            mute.setGraphic(fontAwesome.create(VOLUME_UP));
        }
    }

    @FXML
    void slower(ActionEvent event) {
        final double oldRate = mediaView.getMediaPlayer().getRate();
        final double newRate = oldRate / 2;

        log.trace("Slowing playback down from {} to {}", oldRate, newRate);
        mediaView.getMediaPlayer().setRate(newRate);

        adjustTempoButtons();
    }

    @FXML
    void faster(ActionEvent faster) {
        final double oldRate = mediaView.getMediaPlayer().getRate();
        final double newRate = oldRate * 2;

        log.trace("Speeding playback up from {} to {}", oldRate, newRate);
        mediaView.getMediaPlayer().setRate(newRate);

        adjustTempoButtons();
    }

    private void adjustTempoButtons() {
        final double rate = mediaView.getMediaPlayer().getRate();
        stopWatch.setRate(rate);

        if (rate < 1) {
            if (rate >= 0.5) {
                slower.setGraphic(fontAwesome.create(ANGLE_DOUBLE_LEFT));
            }

            faster.setGraphic(fontAwesome.create(ANGLE_RIGHT));
        } else if (rate == 1) {
            slower.setGraphic(fontAwesome.create(ANGLE_LEFT));
            faster.setGraphic(fontAwesome.create(ANGLE_RIGHT));
        } else {
            if (rate <= 2) {
                faster.setGraphic(fontAwesome.create(ANGLE_DOUBLE_RIGHT));
            }

            slower.setGraphic(fontAwesome.create(ANGLE_LEFT));
        }
    }

    private Media toMedia(File file) {
        Validate.isNotNull(file, "File");

        return new Media(file.toURI().toString());
    }

    public void loadVideo(File file) {
        stopWatch.stop();
        stopWatch.reset();

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
        stopWatch.start();
    }

    public DoubleProperty msPlayed() {
        return stopWatch.elapsedTimeProperty();
    }

    public ReadOnlyObjectProperty<Duration> totalDurationProperty() {
        return mediaView.getMediaPlayer().totalDurationProperty();
    }

    public DoubleProperty codingHeight() {
        return codingHeight;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mute.setGraphic(fontAwesome.create(VOLUME_OFF));
        slower.setGraphic(fontAwesome.create(ANGLE_LEFT));
        faster.setGraphic(fontAwesome.create(ANGLE_RIGHT));
        playpause.setGraphic(fontAwesome.create(PAUSE));
    }
}
