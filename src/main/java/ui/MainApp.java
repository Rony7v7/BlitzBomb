package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class MainApp extends Application {
    private static Stage stage;
    private static Stage auxStage;

    @Override
    public void start(@SuppressWarnings("exports") Stage s) throws IOException {
        stage=s;
        setRoot("main-view","");
    }

    static void setRoot(String fxml) throws IOException {
        setRoot(fxml,stage.getTitle());
    }

    static void setRoot(String fxml, String title) throws IOException {
        Scene scene = new Scene(loadFXML(fxml));
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/"+fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void showWindow(String fxml) throws IOException {

        if (MainApp.isAuxStageOpen()) {
            return;
        }

        auxStage = new Stage();
        Scene scene = new Scene(loadFXML(fxml));
        auxStage.setScene(scene);
        auxStage.show();
    }

    private static boolean isAuxStageOpen(){
        return auxStage!=null;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
