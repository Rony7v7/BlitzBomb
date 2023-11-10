package ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainViewController implements Initializable {

    @FXML
    private Label lblOut;

    @FXML
    private Button playBTN;

    @FXML
    private void play(ActionEvent event) {
        lblOut.setText("tas jugando");
        FXMLLoader loader;
        try {
            loader = MainApp.setRoot("game-view", "Blitz Bomb");
            GameViewController controller = loader.getController();
            controller.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
