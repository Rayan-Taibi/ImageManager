package com.imagemanager.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
    private TextField searchField;

    @FXML
    private Label resultCountLabel;

    private MetadataManager metadataManager;
    private MainController mainController;
    private List<String> allImagePaths;

    private final ObservableList<String> displayedPaths = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        metadataManager = new MetadataManager(new JsonMetadataDAO());

        // Show only filename in list, but keep real absolute path as the item value
        imageListView.setItems(displayedPaths);
        imageListView.setCellFactory(lv -> new ListCell<>() {
            private final HBox container = new HBox();
            private final Label nameLabel = new Label();
            private final Button deleteBtn = new Button("✕");
            
            {
                deleteBtn.setStyle("-fx-font-size: 10; -fx-padding: 0; -fx-text-fill: #888888; -fx-background-color: transparent; -fx-border-width: 0;");
                deleteBtn.setOnAction(e -> {
                    String path = getItem();
                    if (path != null) {
                        deleteImage(path);
                    }
                });
                
                container.setSpacing(5);
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                container.getChildren().addAll(nameLabel, spacer, deleteBtn);
            }
            
            @Override
            protected void updateItem(String path, boolean empty) {
                super.updateItem(path, empty);
                if (empty || path == null) {
                    setGraphic(null);
                } else {
                    nameLabel.setText(new File(path).getName());
                    setGraphic(container);
                }
            }
        });

        imageListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && mainController != null) {
                mainController.loadImage(newVal);
            }
        });

        loadImages();
    }

    /**
     * Set the MainController for communication.
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
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

    public void refresh() {
        metadataManager = new MetadataManager(new JsonMetadataDAO());
        loadImages();
    }

    /**
     * Delete an image from the library.
     */
    private void deleteImage(String imagePath) {
        metadataManager.clearMetadata(imagePath);
        metadataManager.saveAll();
        refresh();
    }
}

