module com.imagemanager {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.imagemanager to javafx.fxml;
    opens com.imagemanager.controller to javafx.fxml;
    exports com.imagemanager;
}
