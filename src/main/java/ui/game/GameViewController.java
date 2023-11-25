package ui.game;

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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import structures.classes.Edge;
import structures.classes.GraphAL;
import structures.classes.GraphAM;
import structures.classes.Vertex;
import structures.interfaces.IGraph;
import ui.MainApp;
import ui.menu.MainViewController;
import model.BombWrapper;
import model.Player;
import model.Timer;
import model.enums.Difficulty;
import model.enums.GameStatus;
import model.enums.GraphType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Controller.LevelGenerator;
import Controller.PowerUpController;
import Controller.external.FileManager;

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

    /**
     * Initializes the game view controller.
     * This method is called when the game view is loaded.
     * It sets up the game environment, initializes the player, file manager,
     * graphics context,
     * power-up controller, and starts a new thread to continuously update the game
     * state.
     *
     * @param location  The URL location of the game view.
     * @param resources The resource bundle containing localized resources for the
     *                  game view.
     */
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

    /**
     * Initializes the timer for the game based on the specified difficulty level.
     *
     * @param difficulty The difficulty level of the game.
     */
    public void initTimer(Difficulty difficulty) {

        timerLabel.setText(timerFormat(secondsRemaining));

        // Calculate the minimum spanning tree of the graph, i.e. the shortest path
        IGraph<String, BombWrapper> MST = graph.prim(graph.getVertexList().get(0));

        int totalTime = graph.DFS(MST);

        switch (difficulty) {
            case EASY -> {
                totalTime += 80;
            }
            case MEDIUM -> {
                totalTime += 60;
            }
            case HARD -> {
                totalTime += 30;
            }
        }
        timer = new Timer(totalTime);
        timer.startTimer(this::updateTimerLabel, this::handleTimerFinish);

        updateTimerLabel(totalTime);
    }

    /**
     * Updates the timer label with the given number of seconds remaining.
     *
     * @param secondsRemaining the number of seconds remaining
     */
    private void updateTimerLabel(int secondsRemaining) {
        this.secondsRemaining = secondsRemaining;
        timerLabel.setText(timerFormat(secondsRemaining));
    }

    /**
     * Formats the given number of seconds into a timer format.
     * The timer format is in the format "MM:SS", where MM represents minutes and SS
     * represents seconds.
     *
     * @param seconds the number of seconds to be formatted
     * @return the formatted timer string
     */
    private String timerFormat(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    /**
     * Handles the event when the timer finishes.
     * Updates the timer label with the remaining seconds.
     * Kills all threads and triggers the game over with a loss due to time.
     */
    private void handleTimerFinish() {
        updateTimerLabel(secondsRemaining);
        try {
            killAllthreads();
            MainApp.gameOver(GameStatus.LOSE_TIME, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops all running threads and saves the player's progress.
     */
    private void killAllthreads() {
        isGameRunning = false;
        timer.stopTimer();
        savePlayer();
    }

    // -------------- VIEW ------------------

    /**
     * Initializes the drawing environment and sets the background color.
     * Draws a rectangle around the canvas to represent the borders.
     * Calls the drawGraph method to draw the graph.
     */
    private void initDraw() {
        gc.setFill(Color.web("#f7efd8")); // Set your desired background color
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.RED);

        // Draw a rectangle around the Canvas to represent the borders
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawGraph(graph);
    }

    /**
     * Draws a graph on the canvas.
     *
     * @param graph the graph to be drawn
     */
    private void drawGraph(IGraph<String, BombWrapper> graph) {

        // clear the canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (graph instanceof GraphAL) {
            drawALGraph(graph);
        } else {
            drawAMGraph(graph);
        }
    }

    /**
     * Draws an adjacency matrix graph on the canvas.
     * 
     * @param graph2 The adjacency matrix graph to be drawn.
     */
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

    /**
     * Draws an ALGraph on the canvas.
     * 
     * @param graph the ALGraph to be drawn
     */
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

    /**
     * Paints an edge on the canvas with a red color.
     * 
     * @param edge The edge to be painted.
     */
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

    /**
     * Highlights the connected vertices of the player's avatar collision with a
     * vertex.
     * If a collision occurs, the method iterates through the edges of the vertex,
     * adds the connected vertices to a list, and highlights them.
     * 
     * This method is used to indicate to which vertex the player's avatar is
     * connected.
     */
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

    /**
     * Highlights a vertex on the game view.
     * If the vertex represents a spawn or end node, it will not be highlighted.
     * The vertex is highlighted by drawing an image on the specified coordinates
     * with the specified dimensions.
     *
     * @param vertex The vertex to be highlighted.
     */
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

    }

    /**
     * Draws the weight of an edge on the game canvas.
     * 
     * @param edge The edge whose weight is to be drawn.
     */
    private void drawEdgeWeight(Edge<String, BombWrapper> edge) {
        double targetX = edge.getVertex2().getValue().X;
        double targetY = edge.getVertex2().getValue().Y;

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

    /**
     * Generates a random graph based on the specified graph type.
     *
     * @param graphType the type of graph to generate (ADJACENCY_LIST or
     *                  ADJACENCY_MATRIX)
     * @return the generated graph
     */
    private IGraph<String, BombWrapper> generateRandomGraph(GraphType graphType) {
        if (graphType.equals(GraphType.ADJACENCY_LIST)) {
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

    /**
     * Retrieves the edges connected to a given vertex.
     *
     * @param vertex The vertex for which to retrieve the edges.
     * @return A list of edges connected to the vertex.
     */
    private List<Edge<String, BombWrapper>> getEdges(Vertex<String, BombWrapper> vertex) {
        if (graph instanceof GraphAL) {
            return vertex.getEdges();
        }
        return ((GraphAM<String, BombWrapper>) graph).getVertexEdges(vertex);
    }

    /**
     * Detects collision between the avatar and a vertex in the graph.
     * If a collision is detected, the bomb associated with the vertex is activated.
     * 
     * @param positionX The x-coordinate of the avatar's position.
     * @param positionY The y-coordinate of the avatar's position.
     * @return The vertex with which the avatar collided, or null if no collision
     *         occurred.
     */
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

    /**
     * Loads the ranking of players from a file and displays it in the UI.
     */
    private void loadRanking() {
        ArrayList<String> playersRanking = fileManager.loadPlayers();
        rankingVBox.setPrefWidth(130);

        for (String player : playersRanking) {
            String[] playerInfo = player.split(":");
            String nickname = playerInfo[0];
            String score = playerInfo[1];

            HBox hBox = new HBox();

            Label label = new Label(nickname);
            hBox.getChildren().add(label);
            HBox.setHgrow(label, Priority.ALWAYS);

            label = new Label(score);
            hBox.getChildren().add(label);

            rankingVBox.getChildren().add(hBox);

            setHBoxStyle(hBox);
        }
    }

    /**
     * Saves the player's information and updates the players ranking.
     * The player's nickname and score are added to the ranking list.
     * The ranking list is then sorted in descending order based on the players'
     * scores.
     * Finally, the updated ranking list is saved.
     */
    private void savePlayer() {
        ArrayList<String> playersRanking = fileManager.loadPlayers();
        playersRanking.add(player.getNickname() + ":" + player.getScore());

        // Sort the players by score
        if (playersRanking.size() > 1) {
            playersRanking.sort((o1, o2) -> {
                // Convert each line of the ranking into a String array by splitting at ":"
                String[] player1 = o1.split(":");
                String[] player2 = o2.split(":");

                // Compare players' scores in descending order
                // (subtract the score of the second player from the score of the first player)
                // This is done to have the player with a higher score appear first in the list
                return Integer.parseInt(player2[1]) - Integer.parseInt(player1[1]);
            });
        }

        fileManager.savePlayers(playersRanking);
    }

    // -------------- GAME STATUS ------------------
    /**
     * Checks the game status to determine if the player has won or lost.
     * If all bombs are detonated and the player has reached the end, the player
     * wins the game.
     * If not all bombs are detonated and the player has reached the end, the player
     * receives a penalty and loses the game.
     */
    private void checkGameStatus() {

        if (checkForAllBombsDetonated() && playerHasReachedEnd()) {
            player.setScore(secondsRemaining);
            handleWinGame();
        } else if (!checkForAllBombsDetonated() && playerHasReachedEnd()) {
            int penaltyTime = (amountOfBombs - amountOfBombsDetonated) * 5;
            secondsRemaining -= penaltyTime;
            player.setScore(-secondsRemaining);
            handleWinGameWithPenality();
        }
    }

    /**
     * Handles the logic for winning the game.
     * This method is called when the game is won by the player.
     * It stops all running threads and displays the game over screen with the
     * player's score.
     */
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

    /**
     * Handles the win game event with penalty.
     * This method is responsible for stopping all threads, triggering the game over
     * event with the WIN_PENALTY status, and passing the player's score.
     * It runs on the JavaFX application thread using Platform.runLater().
     */
    private void handleWinGameWithPenality() {

        Platform.runLater(() -> {
            try {
                killAllthreads();
                MainApp.gameOver(GameStatus.WIN_PENALTY, player.getScore());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Checks if the player has reached the end of the game.
     * 
     * @return true if the player has reached the end, false otherwise.
     */
    private boolean playerHasReachedEnd() {
        Vertex<String, BombWrapper> finalVertex = graph.getVertexList().get(graph.getVertexList().size() - 1);
        double x = finalVertex.getValue().X;
        double y = finalVertex.getValue().Y;
        double radius = finalVertex.getValue().radius;
        if (Math.sqrt(Math.pow(player.getAvatar().getX() - x, 2)
                + Math.pow(player.getAvatar().getY() - y, 2)) <= radius * 2) {
            return true;
        }
        return false;
    }

}
