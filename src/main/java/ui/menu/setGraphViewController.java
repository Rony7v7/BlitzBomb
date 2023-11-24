package ui.menu;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import model.enums.GraphType;
import ui.MainApp;

public class setGraphViewController implements Initializable {

    /**
     * The spinner used for selecting the type of graph.
     */
    @FXML
    private Spinner<GraphType> graphSpinner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSpinner();
    }

    /**
     * Initializes the spinner for selecting the graph type.
     */
    private void initSpinner() {
        graphSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        graphSpinner.editorProperty().get().setAlignment(Pos.CENTER);

        graphSpinner.setValueFactory(new SpinnerValueFactory<GraphType>() {
            @Override
            public void decrement(int steps) {
                if (getValue().equals(GraphType.ADJACENCY_MATRIX)) {
                    setValue(GraphType.ADJACENCY_LIST);
                } else if (getValue().equals(GraphType.ADJACENCY_LIST)) {
                    setValue(GraphType.ADJACENCY_MATRIX);
                }
            }

            @Override
            public void increment(int steps) {
                if (getValue().equals(GraphType.ADJACENCY_LIST)) {
                    setValue(GraphType.ADJACENCY_MATRIX);
                } else if (getValue().equals(GraphType.ADJACENCY_MATRIX)) {
                    setValue(GraphType.ADJACENCY_LIST);
                }
            }
        });

        graphSpinner.getValueFactory().setValue(GraphType.ADJACENCY_LIST);
    }

    /**
     * Handles the save button action event.
     * 
     * @param event the action event triggered by the save button
     */
    @FXML
    public void save(ActionEvent event) {
        MainViewController.setGraph(graphSpinner.getValue());
        MainApp.closeAuxStage();
    }

    /**
     * Handles the cancel button action event.
     * 
     * @param event the action event triggered by the cancel button
     */
    @FXML
    public void cancel(ActionEvent event) {
        MainViewController.setGraph(GraphType.ADJACENCY_LIST);
        MainApp.closeAuxStage();
    }

}
