module ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    opens ui to javafx.fxml;
    exports ui;
}