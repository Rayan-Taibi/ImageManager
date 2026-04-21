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

    private FilterController filterController;
    private Image originalImage;

    @FXML
    public void initialize() {
        // Initialize the FilterController
        filterController = new FilterController();
        filterController.setImageView(imageView);
        filterController.setStatusLabel(labelStatut);
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
        
        // Pass the loaded image to FilterController
        if (filterController != null) {
            filterController.setOriginalImage(image);
        }
        
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

    @FXML
    protected void SymmetrieHorizontale() {
        if (imageView.getImage() != null) {
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

    // ===== FILTER DELEGATION METHODS =====
    // These methods delegate to FilterController

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

    @FXML
    protected void handleSaveImage() {
        if (filterController != null) {
            filterController.handleSaveImage();
        }
    }

}
