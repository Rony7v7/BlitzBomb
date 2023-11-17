package model;

import javafx.scene.image.Image;
import model.enums.TypeOfNode;

public class BombWrapper {
    private Bomb bomb;
    private Image idle;

    public double X;
    public double Y;
    private TypeOfNode type;

    /**
     * Estoy usando este atributo para saber si el nodo esta seleccionado o no y
     * entonces hacerlo mas grande ()
     * Con seleccionado me refiero a que el kelvin puede llegar a ese nodo
     */
    private boolean isSelected;

    public double radius;

    public BombWrapper(double x, double y, Bomb bomb, double radius) {
        this.X = x;
        this.Y = y;
        this.bomb = bomb;
        this.isSelected = false;
        if (this.bomb == null) {
            this.type = TypeOfNode.NORMAL;
        } else {
            this.type = TypeOfNode.BOMB;
        }
        this.radius = radius;
        this.idle = new Image(getClass().getResource("/assets/Graph/Empty_Vertex.png").toExternalForm());
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
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

    public void setBomb(Bomb bomb) {
        if (bomb != null) {
            this.type = TypeOfNode.BOMB;
            this.idle = new Image(getClass().getResource("/assets/Graph/bomb.png").toExternalForm());
        }
        this.bomb = bomb;
    }
}
