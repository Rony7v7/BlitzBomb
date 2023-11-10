package model;

import javafx.scene.image.Image;
import model.enums.TypeOfNode;

public class BombWrapper {
    private Bomb bomb;
    private Image idle;
    public double X;
    public double Y;
    private TypeOfNode type;

    public double radius;

    public BombWrapper(double x, double y, Bomb bomb, double radius) {
        this.X = x;
        this.Y = y;
        this.bomb = bomb;
        this.radius = radius;
        this.type = TypeOfNode.NORMAL;
    }

    public Image getIdle() {
        if (this.bomb == null) {
            if (type.equals(TypeOfNode.SPAWN)) {
                this.idle = new Image(getClass().getResource("/assets/Graph/spawn_node.png").toExternalForm());
            } else if (type.equals(TypeOfNode.END)) {
                this.idle = new Image(getClass().getResource("/assets/Graph/end_node.png").toExternalForm());
            } else {
                this.idle = new Image(getClass().getResource("/assets/Graph/Empty_Vertex.png").toExternalForm());
            }
        } else {
            this.idle = new Image(getClass().getResource("/assets/Graph/bomb.png").toExternalForm());
        }
        return idle;
    }

    public TypeOfNode getType() {
        return type;
    }

    public void setType(TypeOfNode type) {
        this.type = type;
    }

    public Bomb getBomb() {
        return bomb;
    }

    public void setBomb(Bomb bomb) {
        this.bomb = bomb;
    }
}
