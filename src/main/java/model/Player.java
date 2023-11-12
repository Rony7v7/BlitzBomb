package model;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

public class Player {

    private double x;
    private double y;
    private Canvas canvas;
    private int frame;

    private ArrayList<Image> idle;

    public Player(double x, double y, Canvas canvas) {
        this.x = x;
        this.y = y;
        this.idle = new ArrayList<>();
        this.canvas = canvas;
        frame = 0;

        for (int i = 1; i <= 4; i++) {
            this.idle.add(new Image("file:src/main/resources/assets/avatar/kelvin_stand(" + i + ").png"));
        }
    }

    public void paint() {
       canvas.getGraphicsContext2D().drawImage(idle.get(frame%idle.size()), x, y);
       frame++;
    }



}
