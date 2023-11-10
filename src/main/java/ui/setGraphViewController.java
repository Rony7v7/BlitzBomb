package ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class setGraphViewController implements Initializable {

    @FXML
    private Spinner<String> graphSpinner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSpinner(); 
    }

    private void initSpinner() {
        graphSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        graphSpinner.editorProperty().get().setAlignment(Pos.CENTER);
        
        graphSpinner.setValueFactory(new SpinnerValueFactory<String>() {
            @Override
            public void decrement(int steps) {
                if (getValue() == "ADJACENCY LIST") {
                    setValue("ADJACENCY MATRIX");
                } else if (getValue() == "ADJACENCY MATRIX") {
                    setValue("ADJACENCY LIST");
                }
            }

            @Override
            public void increment(int steps) {
                if (getValue() == "ADJACENCY LIST") {
                    setValue("ADJACENCY MATRIX");
                } else if (getValue() == "ADJACENCY MATRIX") {
                    setValue("ADJACENCY LIST");
                }
            }
        });

        graphSpinner.getValueFactory().setValue("ADJACENCY LIST");
    }

    @FXML
    public void save(@SuppressWarnings("exports") ActionEvent event) {
        MainApp.setGraph(graphSpinner.getValue());
    }

    @FXML
    public void cancel(@SuppressWarnings("exports") ActionEvent event) {
        MainApp.setGraph("ADJACENCY LIST");
    }
    
}
