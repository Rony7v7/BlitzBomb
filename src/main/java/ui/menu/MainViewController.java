package ui.menu;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.enums.Difficulty;
import ui.MainApp;
import ui.game.GameViewController;

public class MainViewController implements Initializable {

    @FXML
    private Label lblOut;

    @FXML
    private Button playBTN;

    @FXML
    private TextField nameInput;

    private static String graphType = "ADJACENCY LIST";
    private static Difficulty difficulty = Difficulty.MEDIUM;

    @FXML
    private void play(ActionEvent event) {
        try {
            if (nameInput.getText().strip().equals("")) {
                MainApp.showAlert(AlertType.INFORMATION,"Missing Nickname", "Please enter a nickname");
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

    public static void setGraph(String graph) {
        graphType = graph;
    }

    public static String getGraphType() {
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
