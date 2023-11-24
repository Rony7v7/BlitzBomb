package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.enums.Difficulty;
import model.enums.GameStatus;
import ui.game.GameViewController;
import ui.game.SoundController;
import ui.menu.AlertController;
import ui.menu.MainViewController;

import java.io.IOException;

public class MainApp extends Application {
    private static Stage stage;
    private static Stage auxStage;

    private static SoundController soundController;

    @Override
    public void start(Stage s) throws IOException {
        stage = s;
        setRoot("main-view", "Blitz Bomb");

        // setear icono
        stage.getIcons().add(new javafx.scene.image.Image(MainApp.class.getResourceAsStream("/images/icon.png")));

        // Deshabilitar maximizar
        stage.setResizable(false);

        // Centrar ventana
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

    static void setRoot(String fxml) throws IOException {
        setRoot(fxml, stage.getTitle());
    }

    public static FXMLLoader setRoot(String fxml, String title) throws IOException {
        FXMLLoader loader = loadFXML(fxml);
        Scene scene = new Scene(loader.load());
        loadCss(scene);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();

        return loader;
    }

    private static FXMLLoader loadFXML(String fxml) throws IOException {
        return new FXMLLoader(MainApp.class.getResource("/fxml/" + fxml + ".fxml"));
    }

    private static void loadCss(Scene scene) {
        scene.getStylesheets().add(MainApp.class.getResource("/styles/style.css").toExternalForm());
    }

    public static void showWindow(String fxml) throws IOException {

        // Cerrar ventana auxiliar si esta abierta
        if (isAuxStageOpen()) {
            auxStage.close();
        }

        auxStage = new Stage();
        Scene scene = new Scene(loadFXML(fxml).load());
        auxStage.setScene(scene);
        auxStage.show();
    }

    private static boolean isAuxStageOpen() {
        return auxStage != null;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setDifficulty(Difficulty difficulty) {
        MainViewController.setDifficulty(difficulty);
        auxStage.close();
    }

    public static void setGraph(String graph) {
        MainViewController.setGraph(graph);
        auxStage.close();
    }

    public static void showAlert(Alert.AlertType alertType, String title, String content) {
        AlertController.showAlert(alertType, GameStatus.NOT_STARTED, title, content);
    }

    public static void gameOver(GameStatus status, int score) throws IOException {
        AlertController.showAlert(Alert.AlertType.INFORMATION, status, "",score+"");
        setRoot("main-view", "Blitz Bomb");
    }
}
