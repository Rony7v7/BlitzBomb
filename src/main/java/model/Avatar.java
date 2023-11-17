package model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import structures.classes.Edge;

public class Avatar {

    private double x;
    private double y;
    private Canvas canvas;
    private int frame;

    private ArrayList<Image> idle;

    public Avatar(double x, double y, Canvas canvas) {
        this.idle = new ArrayList<>();
        this.canvas = canvas;
        this.x = x;
        this.y = y;
        frame = 0;

        for (int i = 1; i <= 8; i++) {
            Image image = new Image(
                    getClass().getResource("/assets/avatar/kelvin_stand/kelvin_stand_" + i + ".png").toExternalForm(),
                    70, 70, false, false);
            idle.add(image);
        }
    }

    public double getXForDrawing() {
        return x - idle.get(1).getWidth() / 2;
    }

    public double getYForDrawing() {
        return y - idle.get(1).getHeight() / 2;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void paint() {
        // delete previous frame
        canvas.getGraphicsContext2D().clearRect(getXForDrawing(), getYForDrawing(),
                idle.get(frame % idle.size()).getWidth(),
                idle.get(frame % idle.size()).getHeight());
        canvas.getGraphicsContext2D().drawImage(idle.get(frame % idle.size()), getXForDrawing(), getYForDrawing());
        frame++;
    }

    public void setOnKeyPressed(KeyEvent event, List<Edge<String, BombWrapper>> edges) {
        double currentX = this.x;
        double currentY = this.y;

        switch (event.getCode()) {
            case UP -> {
                edges.stream()
                        .filter(edge -> edge.getVertex2().getValue().Y < currentY)
                        .findFirst()
                        .ifPresent(edge -> {
                            setY(edge.getVertex2().getValue().Y);
                            setX(edge.getVertex2().getValue().X);
                        });
            }
            case DOWN -> {
                edges.stream()
                        .filter(edge -> edge.getVertex2().getValue().Y > currentY)
                        .findFirst()
                        .ifPresent(edge -> {
                            setY(edge.getVertex2().getValue().Y);
                            setX(edge.getVertex2().getValue().X);
                        });
            }
            case LEFT -> {
                edges.stream()
                        .filter(edge -> edge.getVertex2().getValue().X < currentX
                                && edge.getVertex2().getValue().Y == currentY)
                        .findFirst()
                        .ifPresent(edge -> {
                            setY(edge.getVertex2().getValue().Y);
                            setX(edge.getVertex2().getValue().X);
                        });
            }
            case RIGHT -> {

                edges.stream()
                        .filter(edge -> edge.getVertex2().getValue().X > currentX
                                && edge.getVertex2().getValue().Y == currentY)
                        .findFirst()
                        .ifPresent(edge -> {
                            setY(edge.getVertex2().getValue().Y);
                            setX(edge.getVertex2().getValue().X);
                        });
            }
            default -> {
            }
        }
    }

    public void setOnKeyReleased(KeyEvent event) {
    }

}
