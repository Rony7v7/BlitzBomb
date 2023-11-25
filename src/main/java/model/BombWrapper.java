package model;

import javafx.scene.image.Image;
import model.enums.TypeOfNode;

/**
 * The BombWrapper class represents a wrapper for a Bomb object.
 * It contains information about the bomb's position, type, and radius.
 * The BombWrapper class also provides methods to detonate the bomb and retrieve its current state.
 */
public class BombWrapper {
    private Bomb bomb;
    
    private Image idle;
    private Image onBomb;
    private Image detonated;

    public double X;
    public double Y;
    private TypeOfNode type;

    public double radius;

    public BombWrapper(double x, double y, Bomb bomb, double radius) {
        this.X = x;
        this.Y = y;
        this.bomb = bomb;
        if (this.bomb == null) {
            this.type = TypeOfNode.NORMAL;
        } else {
            this.type = TypeOfNode.BOMB;
        }
        this.radius = radius;
        this.idle = new Image(getClass().getResource("/assets/Graph/Empty_Vertex.png").toExternalForm());
        this.onBomb = new Image(getClass().getResource("/assets/Graph/bomb.png").toExternalForm());
        this.detonated = new Image(getClass().getResource("/assets/Graph/activated_vertex.png").toExternalForm());
    }

    public void detonateBomb() {
        this.bomb.setDetonated(true);
        this.idle = detonated;
    }

    public Image getIdle() {
        return idle;
    }

    public TypeOfNode getType() {
        return type;
    }

    public void setIdle(Image idle) {
        this.idle = idle;
    }

    /**
     * Sets the type of the BombWrapper node.
     * 
     * @param type the type of the node
     */
    public void setType(TypeOfNode type) {
        if (type.equals(TypeOfNode.SPAWN)) {
            this.idle = new Image(getClass().getResource("/assets/Graph/spawn_node.png").toExternalForm());
        } else if (type.equals(TypeOfNode.END)) {
            this.idle = new Image(getClass().getResource("/assets/Graph/end_node.png").toExternalForm());
        } else {
            this.idle = new Image(getClass().getResource("/assets/Graph/Empty_Vertex.png").toExternalForm());
        }
        this.type = type;
    }

    public Bomb getBomb() {
        return bomb;
    }

    /**
     * Sets the bomb for this BombWrapper.
     * If the bomb is not null, it sets the type of node to TypeOfNode.BOMB
     * and loads the idle image for the bomb.
     * 
     * @param bomb the bomb to set
     */
    public void setBomb(Bomb bomb) {
        if (bomb != null) {
            this.type = TypeOfNode.BOMB;
            this.idle = onBomb;
        }
        this.bomb = bomb;
    }
}
