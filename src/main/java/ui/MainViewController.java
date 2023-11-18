package ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainViewController implements Initializable {

    @FXML
    private Label lblOut;

    @FXML
    private Button playBTN;

    @FXML
    private TextField nameInput;

    private static String graphType = "ADJACENCY LIST";

    @FXML
    private void play(ActionEvent event) {
        try {
            if (nameInput.getText().strip().equals("")) {
                MainApp.showAlert("Missing NickName", "Please enter a nickname");
                return;
            }
            GameViewController controller = MainApp.setRoot("game-view", "Blitz Bomb").getController();
            controller.setPlayerName(nameInput.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
