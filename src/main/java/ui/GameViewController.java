package ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
import model.enums.Difficulty;
import model.enums.GameStatus;
import model.enums.TypeOfNode;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Controller.FileManager;
import Controller.LevelGenerator;
import Controller.Timer;

public class GameViewController implements Initializable {

    @FXML
    private Canvas canvas;

    @FXML
    private AnchorPane pane;

    @FXML
    private Button powerUp;

    @FXML
    private Label timerLabel;

    @FXML
    private VBox rankingVBox;

    private static GraphicsContext gc;

    private Player player;
    private IGraph<String, BombWrapper> graph;
    private PowerUpController powerUpController;
    private static final int NUM_VERTICES = 51;
    private int amountOfBombs = 0;
    private int amountOfBombsDetonated = 0;

    private FileManager fileManager;

    private Timer timer;
    private int secondsRemaining;
    private static boolean isGameRunning = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.graph = generateRandomGraph(MainViewController.getGraphType());
        this.player = new Player("", 0, canvas); // asignacion temporal
        this.fileManager = new FileManager();
        gc = this.canvas.getGraphicsContext2D();
        isGameRunning = true;
        powerUpController = new PowerUpController(canvas);

        initActions();
        loadRanking();

        new Thread(() -> {
            while (isGameRunning) {
                Platform.runLater(() -> {
                    initDraw();
                    player.paint();
                    highLightConnectedVertex();
                    powerUpController.paintDijkstra();

                });

                checkGameStatus();
                checkForAllBombsDetonated();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @FXML
    public void powerUp() {
        powerUpController.powerUp(graph);
    }

    // -------------- TIMER ------------------

    public void initTimer(Difficulty difficulty) {

        timerLabel.setText(timerFormat(secondsRemaining));

        // Calculate the minimum spanning tree of the graph, i.e. the shortest path
        IGraph<String, BombWrapper> MST = graph.prim(graph.getVertexList().get(0));

        // TODO: QUITAR ESTO Y PONER EL DFS
        int totalTime = 0;
        for (Edge<String, BombWrapper> edge : MST.getEdgeList()) {
            totalTime += edge.getWeight();
        }
        // Calculate the time it takes to traverse the shortest path
        int seconds = graph.DFS(MST);
        System.out.println("Seconds: " + seconds);
        // TODO: QUITAR ESTO Y PONER EL DFS
        
        switch (difficulty) {
            case EASY -> {
                totalTime += 40;
            }
            case MEDIUM -> {
                totalTime += 30;
            }
            case HARD -> {
                totalTime += 20;
            }
        }
        timer = new Timer(totalTime);
        timer.startTimer(this::updateTimerLabel, this::handleTimerFinish);

        updateTimerLabel(totalTime);
    }

    private void updateTimerLabel(int secondsRemaining) {
        this.secondsRemaining = secondsRemaining;
        timerLabel.setText(timerFormat(secondsRemaining));
    }

    private String timerFormat(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private void handleTimerFinish() {
        try {
            killAllthreads();
            MainApp.gameOver(GameStatus.LOSE_TIME,0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void killAllthreads() {
        isGameRunning = false;
        timer.stopTimer();
        savePlayer();
    }

    // -------------- VIEW ------------------

    private void initDraw() {
        gc.setFill(Color.web("#f7efd8")); // Set your desired background color
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.RED);

        // Draw a rectangle around the Canvas to represent the borders
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawGraph(graph);
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

    private void drawAMGraph(IGraph<String, BombWrapper> graph2) {
        GraphAM<String, BombWrapper> auxGraph = (GraphAM<String, BombWrapper>) graph2;
        ArrayList<ArrayList<Edge<String, BombWrapper>>> matrix = auxGraph.getAdjacencyMatrix();
        // Draw vertex
        for (int i = 0; i < matrix.size(); i++) {
            double x = auxGraph.getVertexList().get(i).getValue().X;
            double y = auxGraph.getVertexList().get(i).getValue().Y;
            double radius = auxGraph.getVertexList().get(i).getValue().radius;
            // Draw vertex at (x, y) on the Canvas

            gc.drawImage(auxGraph.getVertexList().get(i).getValue().getIdle(), x - radius, y - radius,
                    radius * 2 + 5, radius * 2 + 5);

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
                }
            }

        }

    }

    private void drawALGraph(IGraph<String, BombWrapper> graph) {
        for (Vertex<String, BombWrapper> vertex : graph.getVertexList()) {
            double x = vertex.getValue().X;
            double y = vertex.getValue().Y;
            double radius = vertex.getValue().radius;
            // Draw vertex at (x, y) on the Canvas
            // Si esta seleccionado hazlo mas grandre sino simplemente graficalo del tamaño
            // del radio
            gc.drawImage(vertex.getValue().getIdle(), x - radius, y - radius, radius * 2 + 5, radius * 2 + 5);

            Text grade = new Text(vertex.getEdges().size() + "");
            grade.setX(vertex.getValue().X);
            grade.setY(vertex.getValue().Y + 30);
            grade.setFont(Font.font(20));

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

    private void paintEdgeRed(Edge<String, BombWrapper> edge) {
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

    private void highLightConnectedVertex() {
        Vertex<String, BombWrapper> vertex = detectAvatarCollisionWithVertex(player.getAvatar().getX(),
                player.getAvatar().getY());
        ArrayList<Vertex<String, BombWrapper>> connectedVertices = new ArrayList<>();
        if (vertex != null) {
            for (Edge<String, BombWrapper> edge : getEdges(vertex)) {
                connectedVertices.add(edge.getVertex2());
                paintEdgeRed(edge);
                drawEdgeWeight(edge);
            }
            for (Vertex<String, BombWrapper> vertex1 : connectedVertices) {
                highLightVertex(vertex1);
            }
        }
    }

    private void highLightVertex(Vertex<String, BombWrapper> vertex) {
        if (vertex.getValue().getType().equals(model.enums.TypeOfNode.SPAWN)
                || vertex.getValue().getType().equals(model.enums.TypeOfNode.END)) {
            return;
        }

        double x = vertex.getValue().X;
        double y = vertex.getValue().Y;
        double radius = vertex.getValue().radius;

        double scaleFactor = 1;
        double height = (radius * 2 * scaleFactor);
        double newWidth = vertex.getValue().getIdle().getWidth()
                * (height / vertex.getValue().getIdle().getHeight());
        double newX = x - newWidth / 2;
        double newY = y - height / 2;

        gc.drawImage(new Image(getClass().getResource("/assets/Graph/highlighted_vertex.png").toExternalForm()), newX,
                newY, newWidth, height);
        vertex.getValue().setSelected(true);

    }

    /**
     * Este es el metodo encargado de poner el peso de la arista en el canvas
     * Calcula el peso del vertice y luego se lo pinta en las cordiandas del vertice
     * 
     * @param edge la arista a la que se le va a poner el peso
     */
    private void drawEdgeWeight(Edge<String, BombWrapper> edge) {
        double targetX = edge.getVertex2().getValue().X;
        double targetY = edge.getVertex2().getValue().Y;

        // Como se va a ver el texto
        Text text = new Text(edge.getWeight() + "");
        text.setFill(Color.RED);
        text.setFont(new Font(32));
        gc.setFill(text.getFill());
        gc.setFont(text.getFont());
        gc.fillText(text.getText(), targetX - 10, targetY - 5);
    }

    private void setHBoxStyle(HBox hBox) {
        hBox.getStyleClass().add("ranking-HBox");
        
        hBox.getChildren().get(0).getStyleClass().add("rankingName-label");
        hBox.getChildren().get(1).getStyleClass().add("rankingScore-label");
    }
    // -------------- CONTROL ------------------

    public void setPlayerName(String text) {
        this.player.setNickname(text);
    }

    private void initActions() {

        pane.setOnKeyPressed(e -> {
            player.setOnKeyPressed(e,
                    getEdges(detectAvatarCollisionWithVertex(player.getAvatar().getX(),
                            player.getAvatar().getY())));
        });
    }

    public static void gameOver() {
        isGameRunning = false;
    }

    private IGraph<String, BombWrapper> generateRandomGraph(String graphType) {
        if (graphType.equals("ADJACENCY LIST")) {
            this.graph = new GraphAL<>();
        } else {
            this.graph = new GraphAM<>();
        }

        LevelGenerator levelGenerator = new LevelGenerator(graph);
        IGraph<String, BombWrapper> graph = levelGenerator.generateRandomLevel(NUM_VERTICES, this.canvas.getHeight(),
                this.canvas.getWidth() + 50);
        this.amountOfBombs = levelGenerator.amountOfBombs();
        return graph;

    }

    private List<Edge<String, BombWrapper>> getEdges(Vertex<String, BombWrapper> vertex) {
        if (graph instanceof GraphAL) {
            return vertex.getEdges();
        }
        return ((GraphAM<String, BombWrapper>) graph).getVertexEdges(vertex);
    }

    private Vertex<String, BombWrapper> detectAvatarCollisionWithVertex(double positionX, double positionY) {
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

    private boolean checkForAllBombsDetonated() {
        return amountOfBombsDetonated == amountOfBombs;
    }

    /**
     * The function activates a bomb in a graph vertex and checks if all bombs have
     * been detonated and the
     * player has reached the end.
     * 
     * @param vertex The `vertex` parameter is an object of type `Vertex<String,
     *               BombWrapper>`. It
     *               represents a vertex in a graph data structure. The vertex
     *               contains a value of type `BombWrapper`,
     *               which is a wrapper class for a bomb object.
     */
    private void activateBomb(Vertex<String, BombWrapper> vertex) {
        if (vertex.getValue().getType().equals(model.enums.TypeOfNode.BOMB)
                && !vertex.getValue().getBomb().isDetonated()) {
            vertex.getValue().detonateBomb();
            amountOfBombsDetonated++;
        }
    }

    private void loadRanking() {
        ArrayList<String> playersRanking = fileManager.loadPlayers();

        for (String player : playersRanking) {
            String[] playerInfo = player.split(":");
            String nickname = playerInfo[0];
            String score = playerInfo[1];

            HBox hBox = new HBox();

            Label label = new Label(nickname);
            hBox.getChildren().add(label);

            label = new Label(score);
            hBox.getChildren().add(label);

            rankingVBox.getChildren().add(hBox);

            setHBoxStyle(hBox);
        }
    }

    private void savePlayer() {
        ArrayList<String> playersRanking = fileManager.loadPlayers();
        playersRanking.add(player.getNickname() + ":" + player.getScore());

        //Sort the players by score
        // if (playersRanking.size() > 2) {
        //     playersRanking.sort((o1, o2) -> {
        //         String[] player1 = o1.split(":");
        //         String[] player2 = o2.split(":");

        //         return Integer.parseInt(player2[2]) - Integer.parseInt(player1[1]);
        //     });
        // }

        fileManager.savePlayers(playersRanking);
    }

    // -------------- GAME STATUS ------------------
    private void checkGameStatus() {
        if (checkForAllBombsDetonated() && playerHasReachedEnd()) {
            handleWinGame();
        } else if (!checkForAllBombsDetonated() && playerHasReachedEnd()) {
            int penaltyTime = (amountOfBombsDetonated - amountOfBombs) * 30;
            secondsRemaining -= penaltyTime;
            handleWinGameWithPenality();
        }
        player.setScore(secondsRemaining);
    }

    private void handleWinGame() {
        Platform.runLater(() -> {
            try {
                killAllthreads();
                MainApp.gameOver(GameStatus.WIN, player.getScore());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void handleWinGameWithPenality() {
        secondsRemaining -= 30;
        Platform.runLater(() -> {
            try {
                killAllthreads();
                MainApp.gameOver(GameStatus.WIN_PENALTY, player.getScore());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private boolean playerHasReachedEnd() {
        for (Vertex<String, BombWrapper> vertex : graph.getVertexList()) {
            if (vertex.getValue().getType().equals(TypeOfNode.END)) {
                double x = vertex.getValue().X;
                double y = vertex.getValue().Y;
                double radius = vertex.getValue().radius;
                if (Math.sqrt(Math.pow(player.getAvatar().getX() - x, 2)
                        + Math.pow(player.getAvatar().getY() - y, 2)) <= radius * 2) {
                    return true;
                }
            }
        }
        return false;
    }

}
