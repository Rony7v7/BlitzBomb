module ui {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.media;
    requires com.google.gson;
    requires java.desktop;

    opens ui to javafx.fxml;
    opens Controller to javafx.fxml;

    exports ui;
    exports model;
    exports model.enums;
}