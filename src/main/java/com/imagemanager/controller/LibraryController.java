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

    private final ObservableList<String> displayedPaths = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        metadataManager = new MetadataManager(new JsonMetadataDAO());

        // Show only filename in list, but keep real absolute path as the item value
        imageListView.setItems(displayedPaths);
        imageListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String path, boolean empty) {
                super.updateItem(path, empty);
                if (empty || path == null) {
                    setText(null);
                } else {
                    setText(new File(path).getName());
                }
            }
        });

        imageListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            currentSelectedPath = newVal;
            if (currentSelectedPath != null) {
                displayPreview(currentSelectedPath);
            }
        });

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
        allImagePaths = metadataManager.getAllMetadata().keySet().stream().sorted().toList();
        displayImages(allImagePaths);
    }

    /**
     * Display a list of image paths in the ListView.
     */
    private void displayImages(List<String> paths) {
        displayedPaths.setAll(paths);
        resultCountLabel.setText("Found: " + paths.size());
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

                if (meta != null && meta.getTags() != null && !meta.getTags().isEmpty()) {
                    String tags = String.join(", ", meta.getTags().stream().map(t -> t.value()).toList());
                    info.append(tags);
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

            if (filterController != null) {
                // This will load the image AND replay saved filters/transforms in order
                filterController.loadImageFromPath(currentSelectedPath);
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
        // Reload from disk so the library reflects latest saved metadata
        metadataManager = new MetadataManager(new JsonMetadataDAO());
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

