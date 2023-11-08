package ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class MainViewController implements Initializable {

    @FXML
    private Label lblOut;

    @FXML
    private void play(ActionEvent event) {
        lblOut.setText("tas jugando");
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
