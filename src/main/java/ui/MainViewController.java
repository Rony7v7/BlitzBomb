package ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainViewController implements Initializable {

    @FXML
    private Label lblOut;

    @FXML
    private Button playBTN;

    private static String graphType = "ADJACENCY LIST";

    @FXML
    private void play(ActionEvent event) {
        try {
            MainApp.setRoot("game-view", "Blitz Bomb");
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
