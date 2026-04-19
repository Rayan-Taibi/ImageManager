package com.example.imagemanagerproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.swing.*;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        Button btnOuvrir = new Button("Ouvrir image");
        Label labelStatut = new Label("Aucune image ouverte");
        HBox barreHaut = new HBox();
        barreHaut.setSpacing(10);
        barreHaut.getChildren().add(btnOuvrir);
        barreHaut.getChildren().add(labelStatut);
        //on met la barre en haut
        root.setTop(barreHaut);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Gestionnaire d'Images");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}