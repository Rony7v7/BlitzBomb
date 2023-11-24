package model;

import javafx.util.Duration;
import java.util.function.Consumer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

/**
 * A class representing a timer that counts down a specified number of seconds.
 */
public class Timer {

    private static int secondsRemaining;
    private Timeline timeline;

    /**
     * Constructs a Timer object with the specified number of seconds.
     *
     * @param seconds the number of seconds for the timer
     */
    public Timer(int seconds) {
        secondsRemaining = seconds;
    }

    /**
     * Starts the timer with the specified actions to be performed on each tick and when the timer finishes.
     *
     * @param onTick   the action to be performed on each tick, accepting the remaining seconds as a parameter
     * @param onFinish the action to be performed when the timer finishes
     */
    public void startTimer(Consumer<Integer> onTick, Runnable onFinish) {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsRemaining--;
            onTick.accept(secondsRemaining);

            if (secondsRemaining <= 0) {
                stopTimer();
                onFinish.run();
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Gets the number of seconds remaining on the timer.
     *
     * @return the number of seconds remaining
     */
    public int getSecondsRemaining() {
        return secondsRemaining;
    }

    /**
     * Sets the number of seconds remaining on the timer.
     *
     * @param seconds the number of seconds to set
     */
    public void setSecondsRemaining(int seconds) {
        secondsRemaining = seconds;
    }

    /**
     * Subtracts the specified number of seconds from the remaining time on the timer.
     *
     * @param seconds the number of seconds to subtract
     */
    public static void substractSeconds(int seconds) {
        secondsRemaining -= seconds;
    }

    /**
     * Stops the timer if it is running.
     */
    public void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }
}
