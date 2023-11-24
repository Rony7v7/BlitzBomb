package ui.menu;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.enums.GameStatus;
import ui.MainApp;

/**
 * The AlertController class is responsible for displaying alert dialogs in the UI.
 * It provides methods to show different types of alerts based on the game status.
 */
public class AlertController {

    // Constants for alert images
    private static final Image WIN_GIF = new Image(AlertController.class.getResource("/images/victory.gif").toString());
    private static final Image WIN_PENALTY_GIF = new Image(AlertController.class.getResource("/images/penalty_victory.gif").toString());
    private static final Image LOSE_GIF = new Image(AlertController.class.getResource("/images/game_lose.gif").toString());

    /**
     * Constructs a new AlertController object.
     */
    public AlertController() {
        
    }

    /**
     * Shows an alert dialog with the specified alert type, game status, title, and content.
     * 
     * @param alertType the type of the alert dialog
     * @param status the game status
     * @param title the title of the alert dialog
     * @param content the content of the alert dialog
     */
    public static void showAlert(Alert.AlertType alertType, GameStatus status, String title, String content) {
        Alert alert = new Alert(alertType);

        setAlertStyle(alert);
        
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(content);
        setAlertByGameStatus(alert, status);

        Platform.runLater(alert::showAndWait);
        
    }

    /**
     * Sets the alert dialog based on the game status.
     * 
     * @param alert the alert dialog
     * @param status the game status
     */
    private static void setAlertByGameStatus(Alert alert, GameStatus status) {

        if (status == GameStatus.NOT_STARTED) {
            return;
        }

        alert.setTitle("Game Over");

        String content = "";
        Image gif = null;

        switch (status) {
            case WIN:
                content = "Congratulations! \n\nYour score: ";
                gif = WIN_GIF;
                break;
            case WIN_PENALTY:
                content = "It was close! \n\nYour score: ";
                gif = WIN_PENALTY_GIF;
                break;
            case LOSE_TIME:
                content = "You lose by time! \n\nYour score: ";
                gif = LOSE_GIF;
                break;
            default:
        }

        if (gif != null) {
            alert.setGraphic(new ImageView(gif));
        }

        alert.setContentText(content+alert.getContentText()+" Kelvin Points");
    }

    /**
     * Sets the style of the alert dialog.
     * 
     * @param alert the alert dialog
     */
    private static void setAlertStyle(Alert alert) {
        alert.getDialogPane().getStylesheets().add(MainApp.class.getResource("/styles/style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("alert");
        alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: #ffffff");
    }
}
