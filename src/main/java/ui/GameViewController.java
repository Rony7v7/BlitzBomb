package ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import structures.classes.Edge;
import structures.classes.GraphAL;
import structures.classes.GraphAM;
// import structures.classes.GraphAM;
import structures.classes.Vertex;
import structures.enums.GraphType;
import structures.interfaces.IGraph;
import model.BombWrapper;
import model.Player;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controller.LevelGenerator;

public class GameViewController implements Initializable {

    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane pane;
    private static final int NUM_VERTICES = 51;
    private Player player;

    private IGraph<String, BombWrapper> graph;

    private static GraphicsContext gc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = this.canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#f7efd8")); // Set your desired background color
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        init(MainViewController.getGraphType());
        player = new Player("",0,canvas);
        player.paint();

        // New tread to draw player
        // new Thread(() -> {
        //     while (true) {
        //         try {
        //             player.paint();
        //             Thread.sleep(100);
        //         } catch (InterruptedException e) {
        //             e.printStackTrace();
        //         }
        //     }
        // }).start();

    }

    // Use this method to send all the data that you need.
    private void init(String graphType) {
        gc.setStroke(Color.RED);

        // Draw a rectangle around the Canvas to represent the borders
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
        this.graph = generateRandomGraph(graphType);

        drawGraph(graph);

    }

    private IGraph<String, BombWrapper> generateRandomGraph(String graphType) {
        if (graphType.equals("ADJACENCY LIST")) {
            this.graph = new GraphAL<>(GraphType.Simple);
        } else {
            this.graph = new GraphAM<>(GraphType.Simple);
        }

        LevelGenerator levelGenerator = new LevelGenerator(graph);
        return levelGenerator.generateRandomLevel(NUM_VERTICES, this.canvas.getHeight(),
                this.canvas.getWidth() + 50);

    }

    private void drawGraph(IGraph<String, BombWrapper> graph) {
        if (graph instanceof GraphAL) {
            drawALGraph(graph);
        } else {
            drawAMGraph(graph);
        }
    }

    private void drawAMGraph(IGraph<String, BombWrapper> graph2) {
        GraphAM<String, BombWrapper> auxGraph = (GraphAM<String, BombWrapper>) graph2;
        ArrayList<ArrayList<Edge<String, BombWrapper>>> matrix = auxGraph.getAdjacencyMatrix();
        System.out.println(matrix.size());
        // Draw vertex
        for (int i = 0; i < matrix.size(); i++) {
            double x = auxGraph.getVertexList().get(i).getValue().X;
            double y = auxGraph.getVertexList().get(i).getValue().Y;
            double radius = auxGraph.getVertexList().get(i).getValue().radius;
            // Draw vertex at (x, y) on the Canvas
            gc.drawImage(auxGraph.getVertexList().get(i).getValue().getIdle(), x - radius, y - radius, radius * 2 + 5,
                    radius * 2 + 5);

            int numEdges = 0;
            for (int j = 0; j < matrix.get(i).size(); j++) {

                if (matrix.get(i).get(j) != null) {
                    double targetX = auxGraph.getVertexList().get(j).getValue().X;
                    double targetY = auxGraph.getVertexList().get(j).getValue().Y;

                    // Calculate the coordinates to start and end the line at the vertex borders
                    double startX = auxGraph.getVertexList().get(i).getValue().X;
                    double startY = auxGraph.getVertexList().get(i).getValue().Y;
                    double endX = targetX;
                    double endY = targetY;

                    // Draw edge from (startX, startY) to (endX, endY) on the Canvas
                    gc.setStroke(Color.web("#273142"));
                    gc.strokeLine(startX, startY, endX, endY);
                    numEdges++;
                }
            }

            Text grade = new Text(numEdges + "");
            grade.setX(auxGraph.getVertexList().get(i).getValue().X);
            grade.setY(auxGraph.getVertexList().get(i).getValue().Y + 30);
            grade.setFont(Font.font(20));
            gc.setFill(Color.BLACK);
            gc.fillText(grade.getText(), auxGraph.getVertexList().get(i).getValue().X,
                    auxGraph.getVertexList().get(i).getValue().Y);
        }

    }

    private void drawALGraph(IGraph<String, BombWrapper> graph) {
        for (Vertex<String, BombWrapper> vertex : graph.getVertexList()) {
            double x = vertex.getValue().X;
            double y = vertex.getValue().Y;
            double radius = vertex.getValue().radius;
            // Draw vertex at (x, y) on the Canvas
            gc.drawImage(vertex.getValue().getIdle(), x - radius, y - radius, radius * 2 + 5, radius * 2 + 5);

            Text grade = new Text(vertex.getEdges().size() + "");
            grade.setX(vertex.getValue().X);
            grade.setY(vertex.getValue().Y + 30);
            grade.setFont(Font.font(20));
            gc.setFill(Color.BLACK);
            gc.fillText(grade.getText(), vertex.getValue().X, vertex.getValue().Y);

            // Image image = new
            // Image(getClass().getResource("/assets/Graph/edge.png").toExternalForm());

            for (Edge<String, BombWrapper> edge : vertex.getEdges()) {
                double targetX = edge.getVertex2().getValue().X;
                double targetY = edge.getVertex2().getValue().Y;

                // Calculate the coordinates to start and end the line at the vertex borders
                double startX = x + radius * Math.cos(Math.atan2(targetY - y, targetX - x));
                double startY = y + radius * Math.sin(Math.atan2(targetY - y, targetX - x));
                double endX = targetX - radius * Math.cos(Math.atan2(targetY - y, targetX - x));
                double endY = targetY - radius * Math.sin(Math.atan2(targetY - y, targetX - x));

                // Draw edge from (startX, startY) to (endX, endY) on the Canvas
                gc.setStroke(Color.web("#273142"));
                gc.strokeLine(startX, startY, endX, endY);
            }
        }
    }

}
