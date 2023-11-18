package Controller;

import javafx.util.Duration;  
import java.util.function.Consumer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

public class Timer {

    private int secondsRemaining;
    private Timeline timeline;

    public Timer(int durationMinutes) {
        this.secondsRemaining = durationMinutes * 60;
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

    public void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }
}
