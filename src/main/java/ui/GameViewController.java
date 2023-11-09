package ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import structures.classes.Edge;
import structures.classes.GraphAL;
import structures.classes.Vertex;
import structures.enums.GraphType;
import structures.interfaces.IGraph;
import model.BombWrapper;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import Controller.LevelGenerator;

public class GameViewController implements Initializable {

    @FXML
    private Canvas canvas;

    private static final int NUM_VERTICES = 10;
    private static final int MIN_EDGES = 1;
    private static final int MAX_EDGES = 4;

    private List<Circle> vertices = new ArrayList<>();
    private List<Line> edges = new ArrayList<>();

    private GraphicsContext gc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IGraph<String, BombWrapper> graph = generateRandomGraph();
        this.gc = canvas.getGraphicsContext2D();

        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;
        // Generar vértices aleatorios
        generateRandomVertices(gc, centerX, centerY);

        // Conectar los vértices aleatoriamente
        connectRandomVertices(gc, centerX, centerY);
    }

    // Use this method to send all the data that you need.
    public void init() {
    }

    private IGraph<String, BombWrapper> generateRandomGraph() {
        IGraph<String, BombWrapper> graph = new GraphAL<>(GraphType.Simple); // You need to provide the appropriate
                                                                             // graph type
        LevelGenerator levelGenerator = new LevelGenerator(graph);
        return levelGenerator.generateRandomLevel(10, 4); // You can adjust the number of vertices and edges as needed
    }

    private void generateRandomVertices(GraphicsContext gc, double centerX, double centerY) {
        Random random = new Random();
        for (int i = 0; i < NUM_VERTICES; i++) {
            double angle = 2 * Math.PI * random.nextDouble(); // Random angle
            double radius = random.nextDouble() * Math.min(centerX, centerY); // Random radius

            double x = centerX + radius * Math.cos(angle); // Calculate X position
            double y = centerY + radius * Math.sin(angle); // Calculate Y position

            Circle vertex = new Circle(x, y, 10); // Círculo para representar el vértice
            vertices.add(vertex);

            // Dibujar el vértice en el Canvas
            gc.setFill(Color.BLUE);
            gc.fillOval(x - 5, y - 5, 20, 20);
        }
    }

    private void connectRandomVertices(GraphicsContext gc, double centerX, double centerY) {
        Random random = new Random();
        for (Circle vertex : vertices) {
            int numEdges = MIN_EDGES + random.nextInt(MAX_EDGES - MIN_EDGES + 1);

            for (int i = 0; i < numEdges; i++) {
                Circle targetVertex = vertices.get(random.nextInt(NUM_VERTICES));
                if (targetVertex != vertex) {
                    double x1 = vertex.getCenterX();
                    double y1 = vertex.getCenterY();
                    double x2 = targetVertex.getCenterX();
                    double y2 = targetVertex.getCenterY();

                    // Dibujar la arista en el Canvas
                    gc.setStroke(Color.RED);
                    gc.strokeLine(x1, y1, x2, y2);
                }
            }
        }
    }
}
