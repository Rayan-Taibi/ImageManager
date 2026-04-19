module com.example.imagemanagerproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.imagemanagerproject to javafx.fxml;
    exports com.example.imagemanagerproject;
}