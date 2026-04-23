module com.imagemanager {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;

    opens com.imagemanager to javafx.fxml;
    opens com.imagemanager.controller to javafx.fxml;
    opens com.imagemanager.model.filter to javafx.fxml;
    opens com.imagemanager.model.image to javafx.fxml;
    opens com.imagemanager.model.metadata to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.imagemanager.model.persistence to javafx.fxml;
    
    exports com.imagemanager;
    exports com.imagemanager.model.metadata to com.fasterxml.jackson.databind;
}
// define module and its dependencies, and specify which packages are open for reflection (for JavaFX) and which are exported for use by other modules.