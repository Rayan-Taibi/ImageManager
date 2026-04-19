module com.example.imagemanagerproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.imagemanagerproject to javafx.fxml;
    exports com.example.imagemanagerproject;
}