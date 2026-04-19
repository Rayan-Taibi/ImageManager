
package com.example.imagemanagerproject.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import java.io.File;

public class MainController {
    @FXML private ImageView imageView;
    @FXML private Label labelStatut;

    @FXML
    protected void handleOpenImage() {
        FileChooser fileChooser = new FileChooser();
        // Optionnel : filtrer pour ne voir que les images
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
            labelStatut.setText("Image chargée : " + selectedFile.getName());
        }
    }
}