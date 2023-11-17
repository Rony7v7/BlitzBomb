package ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import structures.classes.Edge;
import structures.classes.GraphAL;
import structures.classes.GraphAM;
import structures.classes.Vertex;
import structures.interfaces.IGraph;
import model.BombWrapper;
import model.Player;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Controller.LevelGenerator;

public class GameViewController implements Initializable {

    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane pane;
    private static final int NUM_VERTICES = 51;

    private static boolean isGameRunning = false;
    private Player player;

    private IGraph<String, BombWrapper> graph;

    private static GraphicsContext gc;

    private boolean isPowerUpActive = false;

    @FXML
    private Button powerUp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = this.canvas.getGraphicsContext2D();
        this.graph = generateRandomGraph(MainViewController.getGraphType());
        initActions();
        player = new Player("", 0, canvas); // player que llega de la clase controladora
        isGameRunning = true;
        new Thread(() -> {
            while (isGameRunning) {
                Platform.runLater(() -> {
                    initDraw();
                    player.paint();
                    highglightConnectedVertex();
                });
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    // Use this method to send all the data that you need.
    private void initDraw() {
        gc.setFill(Color.web("#f7efd8")); // Set your desired background color
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.RED);

        // Draw a rectangle around the Canvas to represent the borders
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawGraph(graph);
    }

    private void initActions() {

        powerUp.setOnKeyPressed(e -> {

            Vertex<String, BombWrapper> vertex = detectAvatarColisionWithVertex(player.getAvatar().getX(),
                    player.getAvatar().getY());
            ArrayList<Vertex<String, BombWrapper>> connectedVertices = new ArrayList<>();
            if (vertex != null) {
                for (Edge<String, BombWrapper> edge : getEdges(vertex)) {
                    connectedVertices.add(edge.getVertex2());
                }
            }
            connectedVertices.stream().forEach(v -> {
                resetVertexAfterMoved(v);
            });

            player.setOnKeyPressed(e,
                    getEdges(detectAvatarColisionWithVertex(player.getAvatar().getX(),
                            player.getAvatar().getY())));
        });

        canvas.setOnKeyPressed(e ->

        {
            player.setOnKeyPressed(e,
                    getEdges(detectAvatarColisionWithVertex(player.getAvatar().getXForDrawing(),
                            player.getAvatar().getYForDrawing())));
        });

    }

    private void resetVertexAfterMoved(Vertex<String, BombWrapper> vertex) {
        switch (vertex.getValue().getType()) {
            case SPAWN -> {
                vertex.getValue()
                        .setIdle(new Image(getClass().getResource("/assets/Graph/spawn_node.png").toExternalForm()));
            }
            case END -> {
                vertex.getValue()
                        .setIdle(new Image(getClass().getResource("/assets/Graph/end_node.png").toExternalForm()));
            }
            case BOMB -> {
                if (vertex.getValue().getBomb().isDetonated()) {
                    vertex.getValue().setIdle(
                            new Image(getClass().getResource("/assets/Graph/activated_vertex.png").toExternalForm()));
                } else {
                    vertex.getValue()
                            .setIdle(new Image(getClass().getResource("/assets/Graph/bomb.png").toExternalForm()));
                }
            }
            case NORMAL -> {
                vertex.getValue()
                        .setIdle(new Image(getClass().getResource("/assets/Graph/Empty_Vertex.png").toExternalForm()));
            }

            default -> {
            }
        }
    }

    private IGraph<String, BombWrapper> generateRandomGraph(String graphType) {
        if (graphType.equals("ADJACENCY LIST")) {
            this.graph = new GraphAL<>();
        } else {
            this.graph = new GraphAM<>();
        }

        LevelGenerator levelGenerator = new LevelGenerator(graph);
        return levelGenerator.generateRandomLevel(NUM_VERTICES, this.canvas.getHeight(),
                this.canvas.getWidth() + 50);

    }

    private void drawGraph(IGraph<String, BombWrapper> graph) {

        // clear the canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (graph instanceof GraphAL) {
            drawALGraph(graph);
        } else {
            drawAMGraph(graph);
        }
    }

    public static void gameOver() {
        isGameRunning = false;
    }

    @FXML
    @SuppressWarnings("unchecked")
    public void powerUp() {
        Vertex<String, BombWrapper>[] selectedVertices = new Vertex[2]; // Create a new array for selected vertices

        if (!isPowerUpActive) {
            isPowerUpActive = true;

            new Thread(() -> {
                dijkstraPowerUp(selectedVertices);
                isPowerUpActive = false;
            }).start();
        }
    }

    private void dijkstraPowerUp(Vertex<String, BombWrapper>[] selectedVertices) {
        while (isPowerUpActive) {
            canvas.setOnMouseClicked(event -> {
                double mouseX = event.getX();
                double mouseY = event.getY();
                Vertex<String, BombWrapper> clickedVertex = detectVertexClicked(mouseX, mouseY);

                if (clickedVertex != null) {
                    if (selectedVertices[0] == null) {
                        selectedVertices[0] = clickedVertex;
                    } else if (selectedVertices[1] == null) {
                        selectedVertices[1] = clickedVertex;
                        isPowerUpActive = false; // Stop the power-up
                    }
                }
            });

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (selectedVertices[0] != null && selectedVertices[1] != null) {

            List<Edge<String, BombWrapper>> shortestPath = graph.Dijkstra(selectedVertices[0],
                    selectedVertices[1]);

            for (Edge<String, BombWrapper> edge : shortestPath) {
                paintEdgeRed(edge);
            }

            selectedVertices[0] = null;
            selectedVertices[1] = null;
        }
    }

    public void paintEdgeRed(Edge<String, BombWrapper> edge) {
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
    }

    private Vertex<String, BombWrapper> detectVertexClicked(double positionX, double positionY) {
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

    private void drawAMGraph(IGraph<String, BombWrapper> graph2) {
        GraphAM<String, BombWrapper> auxGraph = (GraphAM<String, BombWrapper>) graph2;
        ArrayList<ArrayList<Edge<String, BombWrapper>>> matrix = auxGraph.getAdjacencyMatrix();
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

    public void setPlayerName(String text) {
        this.player.setNickname(text);
    }

    private void highglightConnectedVertex() {
        Vertex<String, BombWrapper> vertex = detectAvatarColisionWithVertex(player.getAvatar().getX(),
                player.getAvatar().getY());
        ArrayList<Vertex<String, BombWrapper>> connectedVertices = new ArrayList<>();
        if (vertex != null) {
            for (Edge<String, BombWrapper> edge : getEdges(vertex)) {
                connectedVertices.add(edge.getVertex2());
                paintEdgeRed(edge);
            }
            for (Vertex<String, BombWrapper> vertex1 : connectedVertices) {
                highlightVertex(vertex1);
            }
        }
    }

    private List<Edge<String, BombWrapper>> getEdges(Vertex<String, BombWrapper> vertex) {
        if (graph instanceof GraphAL) {
            return vertex.getEdges();
        }
        return ((GraphAM<String, BombWrapper>) graph).getVertexEdges(vertex);
    }

    private void highlightVertex(Vertex<String, BombWrapper> vertex) {
        if (vertex.getValue().getType().equals(model.enums.TypeOfNode.SPAWN)
                || vertex.getValue().getType().equals(model.enums.TypeOfNode.END)) {
            return;
        }
        double radius = vertex.getValue().radius;
        double scaleFactor = 2.0;
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        ImageView highlightedVertex = new ImageView(
                getClass().getResource("/assets/Graph/highlighted_vertex.png").toExternalForm());

        // Ajusta la altura de acuerdo al radio del vértice y al factor de escala
        highlightedVertex.setFitHeight(radius * 2 * scaleFactor);

        // Calcula la nueva anchura manteniendo la proporción original de la imagen
        double newWidth = highlightedVertex.getImage().getWidth()
                * (highlightedVertex.getFitHeight() / highlightedVertex.getImage().getHeight());
        highlightedVertex.setFitWidth(newWidth);

        Image highlitedImage = highlightedVertex.snapshot(params, null);

        vertex.getValue().setIdle(highlitedImage);
    }

    private Vertex<String, BombWrapper> detectAvatarColisionWithVertex(double positionX, double positionY) {
        for (Vertex<String, BombWrapper> vertex : graph.getVertexList()) {
            double x = vertex.getValue().X;
            double y = vertex.getValue().Y;
            double radius = vertex.getValue().radius;
            if (Math.sqrt(Math.pow(positionX - x, 2) + Math.pow(positionY - y, 2)) <= radius * 2) {
                activateBomb(vertex);
                return vertex;
            }
        }
        return null;
    }

    private void activateBomb(Vertex<String, BombWrapper> vertex) {
        if (vertex.getValue().getType().equals(model.enums.TypeOfNode.BOMB)) {
            vertex.getValue().getBomb().setDetonated(true);
        }
    }

}
