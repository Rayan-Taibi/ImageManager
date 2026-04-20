package com.imagemanager.controller;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class MainController {
    @FXML
    private ImageView imageView;

    @FXML
    private Label labelStatut;

    @FXML
    protected void handleOpenImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());
        if (selectedFile == null) {
            return;
        }

        Image image = new Image(selectedFile.toURI().toString());
        imageView.setImage(image);
        labelStatut.setText("Image chargée : " + selectedFile.getName());
    }

    @FXML
    protected void RotationDroite() {
        if (imageView.getImage() == null) {
            labelStatut.setText("Charge d'abord une image.");
            return;
        }

        imageView.setRotate(imageView.getRotate() + 90);
        labelStatut.setText("Rotation de +90 appliquee.");
    }

    @FXML
    protected void RotateGauche() {
        if (imageView.getImage() == null) {
            labelStatut.setText("Charge d'abord une image.");
            return;
        }

        imageView.setRotate(imageView.getRotate() - 90);
        labelStatut.setText("Rotation de -90 appliquee.");
    }

    // symmetry controllers
    @FXML
    protected void SymmetrieHorizontale() {
        if (imageView.getImage() != null) {
            // Si c'est déjà à -1, on repasse à 1 (et vice versa)
            double currentScale = imageView.getScaleX();
            imageView.setScaleX(currentScale * -1);
            labelStatut.setText("Effet miroir horizontal appliqué.");
        }
    }
    
    @FXML
    protected void SymmetrieVerticale() {
        if (imageView.getImage() != null) {
            double currentScale = imageView.getScaleY();
            imageView.setScaleY(currentScale * -1);
            labelStatut.setText("Effet miroir vertical appliqué.");
        }
    }

}
