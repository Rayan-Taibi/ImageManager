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
    private FilterController filterController;
    private Image originalImage;

    @FXML
    public void initialize() {
        if (filterController != null) {
            filterController.setImageView(imageView);
            filterController.setStatusLabel(labelStatut);
        }
    }

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
        this.originalImage = image;
        
        // Pass the loaded image AND the file path to FilterController
        if (filterController != null) {
            filterController.setOriginalImage(image);
            filterController.setImagePath(selectedFile.getAbsolutePath());
            // Load transformations from metadata and apply them
            filterController.loadAndApplyTransformations(selectedFile.getAbsolutePath());
            filterController.loadTags();
        }
        
        labelStatut.setText("Image chargée : " + selectedFile.getName());
    }

    @FXML
    protected void handleSepiaFilter() {
        if (filterController != null) {
            filterController.handleSepiaFilter();
        }
    }

    @FXML
    protected void handleNoirBlancFilter() {
        if (filterController != null) {
            filterController.handleNoirBlancFilter();
        }
    }

    @FXML
    protected void handleRGBSwapFilter() {
        if (filterController != null) {
            filterController.handleRGBSwapFilter();
        }
    }

    @FXML
    protected void handlePrewittFilter() {
        if (filterController != null) {
            filterController.handlePrewittFilter();
        }
    }

    @FXML
    protected void handleReset() {
        if (filterController != null) {
            filterController.handleReset();
        }
    }

}

