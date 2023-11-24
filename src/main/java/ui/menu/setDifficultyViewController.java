package ui.menu;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import model.enums.Difficulty;
import ui.MainApp;

/**
 * This class represents the controller for the set difficulty view in the user
 * interface.
 * It allows the user to select the difficulty level for the game.
 */
public class setDifficultyViewController implements Initializable {

    /**
     * The spinner used to select the difficulty level.
     */
    @FXML
    private Spinner<Difficulty> difficultySpinner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSpinner();
    }

    /**
     * Initializes the spinner for selecting the difficulty level.
     */
    private void initSpinner() {
        difficultySpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        difficultySpinner.editorProperty().get().setAlignment(Pos.CENTER);

        difficultySpinner.setValueFactory(new SpinnerValueFactory<Difficulty>() {
            @Override
            public void decrement(int steps) {
                if (getValue() == Difficulty.EASY) {
                    setValue(Difficulty.HARD);
                } else if (getValue() == Difficulty.MEDIUM) {
                    setValue(Difficulty.EASY);
                } else if (getValue() == Difficulty.HARD) {
                    setValue(Difficulty.MEDIUM);
                }
            }

            @Override
            public void increment(int steps) {
                if (getValue() == Difficulty.EASY) {
                    setValue(Difficulty.MEDIUM);
                } else if (getValue() == Difficulty.MEDIUM) {
                    setValue(Difficulty.HARD);
                } else if (getValue() == Difficulty.HARD) {
                    setValue(Difficulty.EASY);
                }
            }
        });

        difficultySpinner.getValueFactory().setValue(Difficulty.MEDIUM);
    }

    /**
     * This is the code that will be executed when the save button is pressed.
     * 
     * @param event the action event triggered by the save button
     */
    @FXML
    public void save(ActionEvent event) {
        Difficulty difficulty = difficultySpinner.getValue();
        MainViewController.setDifficulty(difficulty);
        MainApp.closeAuxStage();
    }

    /**
     * This is the code that will be executed when the cancel button is pressed.
     * 
     * @param event the action event triggered by the cancel button
     */
    @FXML
    public void cancel(ActionEvent event) {
        MainViewController.setDifficulty(Difficulty.EASY);
        MainApp.closeAuxStage();

    }

}
