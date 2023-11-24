package model;

import javafx.util.Duration;
import java.util.function.Consumer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

public class Timer {

    private static int secondsRemaining;
    private Timeline timeline;

    public Timer(int seconds) {
        secondsRemaining = seconds;
    }

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

    public int getSecondsRemaining() {
        return secondsRemaining;
    }

    public void setSecondsRemaining(int seconds) {
        secondsRemaining = seconds;
    }

    public static void substractSeconds(int seconds) {
        secondsRemaining -= seconds;
    }

    public void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }
}
