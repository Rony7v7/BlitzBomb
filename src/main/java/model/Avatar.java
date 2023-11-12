package model;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

public class Avatar {

    private double x;
    private double y;
    private Canvas canvas;
    private int frame;

    private ArrayList<Image> idle;

    public Avatar(double x, double y, Canvas canvas) {
        this.x = x;
        this.y = y;
        this.idle = new ArrayList<>();
        this.canvas = canvas;
        frame = 0;

        for (int i = 1; i <= 8; i++) {
            Image image = new Image(getClass().getResource("/assets/avatar/kelvin_stand/kelvin_stand_"+i+".png").toExternalForm(),70,70,false,false);
            idle.add(image);
        }
    }

    public void paint() {
        // delete previous frame
        canvas.getGraphicsContext2D().clearRect(x, y, idle.get(frame%idle.size()).getWidth(), idle.get(frame%idle.size()).getHeight());
        canvas.getGraphicsContext2D().drawImage(idle.get(frame%idle.size()), x, y);
        frame++;
    }



}
