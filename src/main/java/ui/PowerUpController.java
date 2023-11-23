package ui;

import java.util.List;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.BombWrapper;
import structures.classes.Edge;
import structures.classes.Vertex;
import structures.interfaces.IGraph;

public class PowerUpController {
    private Canvas canvas;
    private Vertex<String, BombWrapper>[] selectedVertices = new Vertex[2];
    private List<Edge<String, BombWrapper>> shortestPath;
    private boolean wasPowerUpUsed;
    private boolean isPowerUpActive;
    private GraphicsContext gc;
    private double mouseX;
    private double mouseY;

    public PowerUpController(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.wasPowerUpUsed = false;
    }

    public void powerUp(IGraph<String, BombWrapper> graph) {
        if (wasPowerUpUsed) {
            return;
        }
        dijkstraPowerUp(graph, selectedVertices);

    }

    public void paintDijkstra() {
        if (!wasPowerUpUsed) {
            return;
        }
        Platform.runLater(() -> {
            for (Edge<String, BombWrapper> edge : shortestPath) {
                paintEdgeRed(edge);
            }
        });
    }

    private void dijkstraPowerUp(IGraph<String, BombWrapper> graph, Vertex<String, BombWrapper>[] selectedVertices) {
        isPowerUpActive = true;
        canvas.setOnMouseClicked(event -> {
            this.mouseX = event.getX();
            this.mouseY = event.getY();
            Vertex<String, BombWrapper> clickedVertex = detectVertexClicked(graph, mouseX, mouseY);

            if (clickedVertex != null) {
                if (selectedVertices[0] == null) {
                    selectedVertices[0] = clickedVertex;
                } else if (selectedVertices[1] == null) {
                    selectedVertices[1] = clickedVertex;
                }
            }
        });
        new Thread(
                () -> {
                    while (isPowerUpActive) {

                        synchronized (this) {
                            if (selectedVertices[0] != null && selectedVertices[1] != null) {
                                List<Edge<String, BombWrapper>> shortestPath = graph.dijkstra(selectedVertices[0],
                                        selectedVertices[1]);
                                this.shortestPath = shortestPath;
                                wasPowerUpUsed = true;
                                isPowerUpActive = false;
                                selectedVertices[0] = null;
                                selectedVertices[1] = null;
                                break;
                            }
                        }

                    }

                }).start();

    }

    private Vertex<String, BombWrapper> detectVertexClicked(IGraph<String, BombWrapper> graph, double positionX,
            double positionY) {
        for (Vertex<String, BombWrapper> vertex : graph.getVertexList()) {
            double x = vertex.getValue().X;
            double y = vertex.getValue().Y;
            double radius = vertex.getValue().radius;
            if (Math.sqrt(Math.pow(positionX - x, 2) + Math.pow(positionY - y, 2)) <= radius) {
                return vertex;
            }
        }
        return null;
    }

    private void paintEdgeRed(Edge<String, BombWrapper> edge) {
        Platform.runLater(() -> {
            double targetX = edge.getVertex2().getValue().X;
            double targetY = edge.getVertex2().getValue().Y;

            // Calculate the coordinates to start and end the line at the vertex borders
            double startX = edge.getVertex1().getValue().X;
            double startY = edge.getVertex1().getValue().Y;
            double endX = targetX;
            double endY = targetY;

            // Draw edge from (startX, startY) to (endX, endY) on the Canvas
            gc.setStroke(Color.RED);
            gc.strokeLine(startX, startY, endX, endY);
        });

    }

}
