package com.imagemanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Point d'entrée de l'application.
 *
 * Charge la vue principale définie dans `fxml/main.fxml` et affiche la fenêtre.
 * Les contrôleurs sont injectés par le fichier FXML (MainController, LibraryController,
 * FilterController) et coordonnent le flux entre l'UI, la gestion des métadonnées
 * et les filtres d'image.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader chargeurFXML = new FXMLLoader(Main.class.getResource("/fxml/main.fxml"));
        Scene scenePrincipale = new Scene(chargeurFXML.load(), 1024, 600);
        stage.setTitle("Gestionnaire d'Images");
        stage.setResizable(true);
        stage.setScene(scenePrincipale);
        stage.show();
    }

    public static void main(String[] arguments) {
        launch(arguments);
    }
}
