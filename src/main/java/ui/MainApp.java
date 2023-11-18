package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.enums.Difficulty;

import java.io.IOException;

public class MainApp extends Application {
    private static Stage stage;
    private static Stage auxStage;

    @Override
    public void start(Stage s) throws IOException {
        stage = s;
        setRoot("main-view", "Blitz Bomb");
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
        stage.setOnCloseRequest(e -> {
            if (isAuxStageOpen()) {
                auxStage.close();
            }
            GameViewController.gameOver();
        });
        return loader;
    }

    private static FXMLLoader loadFXML(String fxml) throws IOException {
        return new FXMLLoader(MainApp.class.getResource("/fxml/" + fxml + ".fxml"));
    }

    private static void loadCss(Scene scene) {
        scene.getStylesheets().add(MainApp.class.getResource("/styles/style.css").toExternalForm());
        Font.loadFont(MainApp.class.getResource("/fonts/kenvector_future.ttf").toExternalForm(), 10);
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
        auxStage.close();
    }

    public static void setGraph(String graph) {
        MainViewController.setGraph(graph);
        auxStage.close();
    }

    public static void showAlert(String titel,String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titel);
        alert.setContentText(content);
        Platform.runLater(alert::showAndWait);
    }

    public static void showWarning(String titel,String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titel);
        alert.setContentText(content);
        Platform.runLater(alert::showAndWait);
    }

    public static void gameOver() throws IOException {
        showWarning("Game Over", "Your time is over :p");
        setRoot("main-view", "Blitz Bomb");
    }
}
