package ui.menu;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.enums.Difficulty;
import model.enums.GraphType;
import ui.MainApp;
import ui.game.GameViewController;

/**
 * The controller class for the main view of the application.
 * This class handles user interactions and controls the flow of the game.
 */
public class MainViewController {

    @FXML
    private Button playBTN;

    @FXML
    private TextField nameInput;

    private static GraphType graphType = GraphType.ADJACENCY_LIST;
    private static Difficulty difficulty = Difficulty.EASY;

    /**
     * Handles the play button action event.
     * If the nickname input is empty, it shows an information alert.
     * Otherwise, it sets the player name, initializes the game timer, and switches to the game view.
     *
     * @param event the action event triggered by the play button
     */
    @FXML
    private void play(ActionEvent event) {
        try {
            if (nameInput.getText().strip().equals("")) {
                MainApp.showAlert(AlertType.INFORMATION, "Missing Nickname", "Please enter a nickname");
                return;
            }
            GameViewController controller = MainApp.setRoot("game-view", "Blitz Bomb").getController();
            controller.setPlayerName(nameInput.getText());
            controller.initTimer(difficulty);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Difficulty getDifficulty() {
        return difficulty;
    }

    public static void setDifficulty(Difficulty difficulty) {
        MainViewController.difficulty = difficulty;
    }

    public static void setGraph(GraphType grap) {
        MainViewController.graphType = grap;
    }

    public static GraphType getGraphType() {
        return graphType;
    }

    @FXML
    private void setDifficulty(ActionEvent event) throws IOException {
        MainApp.showWindow("setDifficulty-view");
    }

    @FXML
    private void setGraph(ActionEvent event) throws IOException {
        MainApp.showWindow("setGraph-view");
    }

}
