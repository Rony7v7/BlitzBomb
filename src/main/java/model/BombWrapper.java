package model;

import javafx.scene.image.Image;

public class BombWrapper {
    private Bomb bomb;
    private Image idle;
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

    public Image getIdle() {
        if (this.bomb == null) {
            return new Image(getClass().getResource("/assets/Graph/Empty_Vertex.png").toExternalForm());
        }
        return new Image(getClass().getResource("/assets/Graph/bomb.png").toExternalForm());
    }

    public Bomb getBomb() {
        return bomb;
    }

    public void setBomb(Bomb bomb) {
        this.bomb = bomb;
    }
}
