package model;

public class BombWrapper {
    private Bomb bomb;
    public double X;
    public double Y;

    public BombWrapper(double x, double y, Bomb bomb) {
        this.X = x;
        this.Y = y;
        this.bomb = bomb;
    }

    public BombWrapper(double x, double y) {
        this.X = x;
        this.Y = y;
        this.bomb = null;
    }

    public Bomb getBomb() {
        return bomb;
    }
}
