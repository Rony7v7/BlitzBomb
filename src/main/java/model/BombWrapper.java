package model;

import javafx.scene.image.Image;

public class BombWrapper {
    private Bomb bomb;
    public Image idle;
    public double X;
    public double Y;

    public double radius;

    public BombWrapper(double x, double y, Bomb bomb, double radius) {
        this.X = x;
        this.Y = y;
        this.bomb = bomb;
        this.radius = radius;
        this.idle = new Image(getClass().getResource("/assets/Graph/Empty_Vertex.png").toExternalForm());
    }

    public BombWrapper(double x, double y, double radius) {
        this.X = x;
        this.Y = y;
        this.bomb = null;
        this.radius = radius;
    }

    public Bomb getBomb() {
        return bomb;
    }
}
