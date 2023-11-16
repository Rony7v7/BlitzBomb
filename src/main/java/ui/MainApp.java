package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
        alert.showAndWait();
    }

}
