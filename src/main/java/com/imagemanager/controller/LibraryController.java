package com.imagemanager.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.imagemanager.model.metadata.MetadataManager;
import com.imagemanager.model.metadata.ImageMetadata;
import com.imagemanager.model.persistence.JsonMetadataDAO;
import java.io.File;
import java.util.List;

/**
 * Controller for the image library panel.
 * Handles browsing, searching, and loading images from metadata.
 */
public class LibraryController {
    @FXML
    private ListView<String> imageListView;

    @FXML
    private ImageView previewImageView;

    @FXML
    private Label infoLabel;

    @FXML
    private TextField searchField;

    @FXML
    private Label resultCountLabel;

    private MetadataManager metadataManager;
    private FilterController filterController;
    private String currentSelectedPath;
    private List<String> allImagePaths;

    @FXML
    public void initialize() {
        metadataManager = new MetadataManager(new JsonMetadataDAO());
        loadImages();
    }

    /**
     * Set the FilterController for image loading and editing.
     */
    public void setFilterController(FilterController filterController) {
        this.filterController = filterController;
    }

    /**
     * Load all images from metadata file.
     */
    private void loadImages() {
        allImagePaths = metadataManager.getAllMetadata().keySet().stream().toList();
        displayImages(allImagePaths);
    }

    /**
     * Display a list of image paths in the ListView.
     */
    private void displayImages(List<String> paths) {
        ObservableList<String> items = FXCollections.observableArrayList();
        
        for (String path : paths) {
            File file = new File(path);
            items.add(file.getName() + " (" + path + ")");
        }

        imageListView.setItems(items);
        resultCountLabel.setText("Found: " + paths.size());

        // Set selection listener
        imageListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.intValue() >= 0 && newVal.intValue() < paths.size()) {
                currentSelectedPath = paths.get(newVal.intValue());
                displayPreview(currentSelectedPath);
            }
        });
    }

    /**
     * Display preview and info for selected image.
     */
    private void displayPreview(String imagePath) {
        try {
            File file = new File(imagePath);
            if (file.exists()) {
                Image img = new Image(file.toURI().toString());
                previewImageView.setImage(img);

                // Display metadata info
                ImageMetadata meta = metadataManager.getMetadata(imagePath);
                StringBuilder info = new StringBuilder("File: " + file.getName() + "\n");
                info.append("Path: ").append(imagePath).append("\n");
                info.append("Tags: ");
                
                if (meta != null && !meta.getTags().isEmpty()) {
                    meta.getTags().forEach(tag -> info.append(tag.value()).append(", "));
                } else {
                    info.append("(none)");
                }

                infoLabel.setText(info.toString());
            } else {
                infoLabel.setText("File not found: " + imagePath);
            }
        } catch (Exception e) {
            infoLabel.setText("Error loading preview: " + e.getMessage());
        }
    }

    /**
     * Search images by tag.
     */
    @FXML
    public void handleSearch() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        
        if (searchTerm.isEmpty()) {
            displayImages(allImagePaths);
            return;
        }

        List<String> filtered = allImagePaths.stream()
            .filter(path -> {
                ImageMetadata meta = metadataManager.getMetadata(path);
                if (meta == null) return false;
                return meta.getTags().stream()
                    .anyMatch(tag -> tag.value().toLowerCase().contains(searchTerm));
            })
            .toList();

        displayImages(filtered);
    }

    /**
     * Clear search and show all images.
     */
    @FXML
    public void handleClearSearch() {
        searchField.clear();
        displayImages(allImagePaths);
    }

    /**
     * Load selected image for editing.
     */
    @FXML
    public void handleLoadForEditing() {
        if (currentSelectedPath == null) {
            showError("Please select an image first");
            return;
        }

        try {
            File file = new File(currentSelectedPath);
            if (!file.exists()) {
                showError("File not found: " + currentSelectedPath);
                return;
            }

            Image img = new Image(file.toURI().toString());
            
            if (filterController != null) {
                filterController.setOriginalImage(img);
                filterController.setCurrentImage(currentSelectedPath);
            }

            showInfo("Loaded: " + file.getName());
        } catch (Exception e) {
            showError("Error loading image: " + e.getMessage());
        }
    }

    /**
     * Refresh library from metadata file.
     */
    @FXML
    public void handleRefresh() {
        loadImages();
        showInfo("Library refreshed");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setContentText(message);
        alert.showAndWait();
    }
}

