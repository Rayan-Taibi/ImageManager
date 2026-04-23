package com.imagemanager.controller;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class MainController {
    @FXML
    private ImageView imageView;

    @FXML
    private Label labelStatut;

    @FXML
    private FilterController filterController;

    @FXML
    private LibraryController libraryController;

    @FXML
    public void initialize() {
        if (filterController != null) {
            filterController.setImageView(imageView);
            filterController.setMainStatusLabel(labelStatut);
            filterController.setLibraryController(libraryController);
        }
        if (libraryController != null) {
            libraryController.setMainController(this);
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

        if (filterController != null) {
            filterController.loadImageFromPath(selectedFile.getAbsolutePath());
        }

        labelStatut.setText("Image chargée : " + selectedFile.getName());
    }
public void loadImage(String imagePath) {
        if (filterController != null) {
            filterController.loadImageFromPath(imagePath);
        }
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

