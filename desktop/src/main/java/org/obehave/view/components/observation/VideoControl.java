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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VideoControl extends BorderPane implements Initializable{
    private static final Logger log = LoggerFactory.getLogger(VideoControl.class);
    private final DoubleProperty msPlayed = new SimpleDoubleProperty(this, "msPlayed", 0);
    private final DoubleProperty codingHeight = new SimpleDoubleProperty(this, "codingHeight", 0);

    private ChangeListener<Duration> currentTimeListener = (observable, oldValue, newValue) -> msPlayed.setValue(newValue.toMillis());

    private GlyphFont fontAwesome = new FontAwesome(getClass().getClassLoader()
            .getResourceAsStream("/org/obehave/view/font/awesome-4.3.0/fonts/FontAwesome.otf"));

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
            playpause.setGraphic(fontAwesome.create(FontAwesome.Glyph.PAUSE));
        } else {
            log.trace("Playing video at {}s", mediaView.getMediaPlayer().getCurrentTime().toSeconds());
            mediaView.getMediaPlayer().play();
            playpause.setGraphic(fontAwesome.create(FontAwesome.Glyph.PLAY));
        }
    }

    @FXML
    void mute() {
        final double volume = mediaView.getMediaPlayer().getVolume();

        if (volume == 0) {
            log.trace("Enabling sound for video");
            mediaView.getMediaPlayer().setVolume(1);
            mute.setText("");
            mute.setGraphic(fontAwesome.create(FontAwesome.Glyph.VOLUME_UP));
        } else {
            log.trace("Disabling sound for video");
            mediaView.getMediaPlayer().setVolume(0);
            mute.setText("");
            mute.setGraphic(fontAwesome.create(FontAwesome.Glyph.VOLUME_OFF));
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

        if (rate < 1) {
            if (rate >= 0.5) {
                slower.setGraphic(fontAwesome.create(FontAwesome.Glyph.ANGLE_DOUBLE_LEFT));
            }

            faster.setGraphic(fontAwesome.create(FontAwesome.Glyph.ANGLE_RIGHT));
        } else if (rate == 1) {
            slower.setGraphic(fontAwesome.create(FontAwesome.Glyph.ANGLE_LEFT));
            faster.setGraphic(fontAwesome.create(FontAwesome.Glyph.ANGLE_RIGHT));
        } else {
            if (rate <= 2) {
                faster.setGraphic(fontAwesome.create(FontAwesome.Glyph.ANGLE_DOUBLE_RIGHT));
            }

            slower.setGraphic(fontAwesome.create(FontAwesome.Glyph.ANGLE_LEFT));
        }
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mute.setGraphic(fontAwesome.create(FontAwesome.Glyph.VOLUME_UP));
        slower.setGraphic(fontAwesome.create(FontAwesome.Glyph.ANGLE_LEFT));
        faster.setGraphic(fontAwesome.create(FontAwesome.Glyph.ANGLE_RIGHT));
        playpause.setGraphic(fontAwesome.create(FontAwesome.Glyph.PLAY));
    }
}
