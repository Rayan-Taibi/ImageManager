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
}
