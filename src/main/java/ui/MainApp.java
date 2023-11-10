package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        return loader;
    }

    private static FXMLLoader loadFXML(String fxml) throws IOException {
        return new FXMLLoader(MainApp.class.getResource("/fxml/" + fxml + ".fxml"));

    }

    public static void showWindow(String fxml) throws IOException {

        if (MainApp.isAuxStageOpen()) {
            return;
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

}
