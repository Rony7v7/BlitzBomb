package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.enums.GameStatus;
import ui.game.GameViewController;
import ui.game.SoundController;
import ui.menu.AlertController;

import java.io.IOException;

/**
 * The MainApp class is the entry point of the application.
 * It extends the Application class and sets up the main stage and other UI components.
 */
public class MainApp extends Application {
    private static Stage stage;
    private static Stage auxStage;

    private static SoundController soundController;

    @Override
    public void start(Stage s) throws IOException {
        stage = s;
        setRoot("main-view", "Blitz Bomb");

        // Set icon
        stage.getIcons().add(new javafx.scene.image.Image(MainApp.class.getResourceAsStream("/images/icon.png")));

        stage.setResizable(false);

        // Center window
        stage.setX(Screen.getPrimary().getVisualBounds().getMaxX() / 2 - stage.getWidth() / 2);
        stage.setY(Screen.getPrimary().getVisualBounds().getMaxY() / 2 - stage.getHeight() / 2);

        stage.setOnCloseRequest(e -> {
            if (isAuxStageOpen()) {
                auxStage.close();
            }
            GameViewController.gameOver();
        });

        soundController = new SoundController("/audio/BackgroundThemeSong.wav", true);
        soundController.play(-20f);
    }

    /**
     * Sets the root FXML file for the application.
     * 
     * @param fxml the path to the FXML file
     * @throws IOException if an I/O error occurs while loading the FXML file
     */
    static void setRoot(String fxml) throws IOException {
        setRoot(fxml, stage.getTitle());
    }

    /**
        * Loads an FXML file and returns an FXMLLoader object.
        * 
        * @param fxml the path to the FXML file
        * @param title the title of the application window
        * @return the FXMLLoader object used to load the FXML file
        * @throws IOException if an I/O error occurs while loading the FXML file
        */
    public static FXMLLoader setRoot(String fxml, String title) throws IOException {
        FXMLLoader loader = loadFXML(fxml);
        Scene scene = new Scene(loader.load());
        loadCss(scene);
        stage.setTitle(title);
        stage.setScene(scene);

        // Center window in current screen
        stage.setX(Screen.getPrimary().getVisualBounds().getMaxX() / 2 - stage.getWidth() / 2);
        stage.setY(Screen.getPrimary().getVisualBounds().getMaxY() / 2 - stage.getHeight() / 2);

        stage.show();

        return loader;
    }

    /**
     * Loads an FXML file using the FXMLLoader class.
     *
     * @param fxml the name of the FXML file to load
     * @return an instance of FXMLLoader that loads the specified FXML file
     * @throws IOException if an I/O error occurs while loading the FXML file
     */
    private static FXMLLoader loadFXML(String fxml) throws IOException {
        return new FXMLLoader(MainApp.class.getResource("/fxml/" + fxml + ".fxml"));
    }

    /**
     * Loads the CSS file and applies it to the given scene.
     *
     * @param scene The scene to apply the CSS to.
     */
    private static void loadCss(Scene scene) {
        scene.getStylesheets().add(MainApp.class.getResource("/styles/style.css").toExternalForm());
    }

    /**
     * Shows a new window with the given FXML file.
     *
     * @param fxml the name of the FXML file to load
     * @throws IOException if an I/O error occurs while loading the FXML file
     */
    public static void showWindow(String fxml) throws IOException {

        // Close the auxiliar window if opened.
        if (isAuxStageOpen()) {
            auxStage.close();
        }

        auxStage = new Stage();
        Scene scene = new Scene(loadFXML(fxml).load());
        auxStage.setScene(scene);
        auxStage.setResizable(false);
        auxStage.show();
    }

    /**
     * Returns true if the auxiliar window is open.
     *
     * @return true if the auxiliar window is open
     */
    private static boolean isAuxStageOpen() {
        return auxStage != null;
    }

    /**
     * Returns the main stage of the application.
     *
     * @return the main stage of the application
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Returns the main stage of the application.
     *
     * @return the main stage of the application
     */
    public static void closeAuxStage() {
        auxStage.close();
    }

    /**
     * Shows an alert dialog with the given parameters.
     *
     * @param alertType the type of the alert dialog
     * @param title the title of the alert dialog
     * @param content the content of the alert dialog
     */
    public static void showAlert(Alert.AlertType alertType, String title, String content) {
        AlertController.showAlert(alertType, GameStatus.NOT_STARTED, title, content);
    }

    /**
     * Shows an alert dialog with the given parameters.
     * This should be used when the game is over.
     *
     * @param alertType the type of the alert dialog
     * @param status the game status
     * @param title the title of the alert dialog
     * @param content the content of the alert dialog
     */
    public static void gameOver(GameStatus status, int score) throws IOException {
        AlertController.showAlert(Alert.AlertType.INFORMATION, status, "", score + "");
        setRoot("main-view", "Blitz Bomb");
    }
}
