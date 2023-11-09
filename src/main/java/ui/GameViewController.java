package ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import structures.classes.Edge;
import structures.classes.GraphAL;
import structures.classes.Vertex;
import structures.enums.GraphType;
import structures.interfaces.IGraph;
import model.BombWrapper;

import java.net.URL;
import java.util.ResourceBundle;

import Controller.LevelGenerator;

public class GameViewController implements Initializable {

    @FXML
    private Canvas canvas;

    private static final int NUM_VERTICES = 51;
    private static final int MAX_EDGES = 4;

    private static GraphicsContext gc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = this.canvas.getGraphicsContext2D();

        IGraph<String, BombWrapper> graph = generateRandomGraph();
        drawGraph(graph);
    }

    // Use this method to send all the data that you need.
    public void init() {
    }

    private IGraph<String, BombWrapper> generateRandomGraph() {
        IGraph<String, BombWrapper> graph = new GraphAL<>(GraphType.Simple); // You need to provide the appropriate
                                                                             // graph type
        LevelGenerator levelGenerator = new LevelGenerator(graph);
        return levelGenerator.generateRandomLevel(NUM_VERTICES, MAX_EDGES, this.canvas.getHeight(),
                this.canvas.getWidth()); // You
        // can

    }

    private void drawGraph(IGraph<String, BombWrapper> graph) {
        for (Vertex<String, BombWrapper> vertex : graph.getVertexList()) {
            Text grade = new Text(vertex.getEdges().size()+ "");
            grade.setX(vertex.getValue().X);
            grade.setY(vertex.getValue().Y);
            gc.setFill(Color.BLACK);
            gc.fillText(grade.getText(), vertex.getValue().X, vertex.getValue().Y);
            double x = vertex.getValue().X;
            double y = vertex.getValue().Y;
            double radius = vertex.getValue().radius;
            // Draw vertex at (x, y) on the Canvas
            gc.setFill(Color.BLUE);
            gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);

            for (Edge<String, BombWrapper> edge : vertex.getEdges()) {
                double targetX = edge.getVertex2().getValue().X;
                double targetY = edge.getVertex2().getValue().Y;

                // Draw edge from (x, y) to (targetX, targetY) on the Canvas
                gc.setStroke(Color.RED);
                gc.strokeLine(x, y, targetX, targetY);
            }
        }
    }

}
