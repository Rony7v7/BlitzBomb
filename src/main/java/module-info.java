module ui {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.media;
    requires com.google.gson;
    requires java.desktop;

    opens ui to javafx.fxml;
    opens Controller to javafx.fxml, com.google.gson;
    opens model to com.google.gson;
    opens model.enums to com.google.gson;

    exports ui;
    exports model;
    exports model.enums;
}