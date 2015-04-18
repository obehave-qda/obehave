package org.obehave.view.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.util.Duration;

public class StopWatch {
    private final Timeline timeline;
    private final TimeProvider timeProvider;
    private final LongProperty elapsedTime = new SimpleLongProperty(this, "elapsedTime", 0);

    private long elapsed = 0;
    private long started = 0;

    /**
     * Creates a new {@code Timer} instance using {@link System#currentTimeMillis()} as {@link StopWatch.TimeProvider}
     */
    public StopWatch() {
        this(System::currentTimeMillis);
    }

    /**
     * Creates a new {@code Timer} instance using {@code timeProvider} as {@link StopWatch.TimeProvider}
     * @param timeProvider the timeProvider to use
     */
    public StopWatch(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1), e -> {
            update();
        }));
    }

    /**
     * Starts the timer
     */
    public void start() {
        started = 0;

        timeline.playFromStart();
    }

    /**
     * Stops the timer
     */
    public void stop() {
        timeline.stop();

        elapsed += current();
        started = 0;

        update();
    }

    /**
     * Resets the timer to zero.
     * Keep in mind that your timer will continue to run, if it wasn't stopped with {@link StopWatch#stop()}
     */
    public void reset() {
        setElapsedTime(0);
    }

    public void setElapsedTime(long elapsedTime) {
        elapsed = elapsedTime;
        started = timeProvider.getTime();

        update();
    }

    public long getElapsedTime() {
        return elapsedTime.get();
    }

    public LongProperty elapsedTimeProperty() {
        return elapsedTime;
    }

    private long current() {
        return started != 0 ? timeProvider.getTime() - started : 0;
    }

    private void update() {
        elapsedTimeProperty().set(elapsed + current());
    }

    @FunctionalInterface
    public interface TimeProvider {
        long getTime();
    }
}
