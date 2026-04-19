module com.example.imagemanagerproject {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.imagemanagerproject to javafx.fxml;
    opens com.example.imagemanagerproject.controller to javafx.fxml;
    exports com.example.imagemanagerproject;
}