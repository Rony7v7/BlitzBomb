package ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import model.enums.Difficulty;

public class setDifficultyViewController implements Initializable {

    @FXML
    private Spinner<Difficulty> difficultySpinner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSpinner(); 
    }

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

    @FXML
    public void save(@SuppressWarnings("exports") ActionEvent event) {
        Difficulty difficulty = difficultySpinner.getValue();
        MainApp.setDifficulty(difficulty);
    }

    @FXML
    public void cancel(@SuppressWarnings("exports") ActionEvent event) {
        MainApp.setDifficulty(Difficulty.MEDIUM);
    }
    
}
