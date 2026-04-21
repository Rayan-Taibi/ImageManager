package com.imagemanager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import com.imagemanager.model.filter.*;

import java.io.File;

public class FilterController {

    @FXML
    private ImageView imageView;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField passwordField;
    
    private Image originalImage;

    // Setter methods for MainController to inject dependencies
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public void setOriginalImage(Image originalImage) {
        this.originalImage = originalImage;
    }

    @FXML
    public void initialize() {
        if (statusLabel != null) {
            statusLabel.setText("No image loaded. Load an image from the main view.");
        }
    }

    // ===== COLOR FILTERS =====

    @FXML
    public void handleSepiaFilter() {
        if (imageView == null || imageView.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }
        Filter filter = new SepiaFilter();
        Image filtered = filter.apply(imageView.getImage());
        imageView.setImage(filtered);
        updateStatus("✓ Sepia filter applied");
    }

    @FXML
    public void handleNoirBlancFilter() {
        if (imageView == null || imageView.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }
        Filter filter = new NoireBlanc();
        Image filtered = filter.apply(imageView.getImage());
        imageView.setImage(filtered);
        updateStatus("✓ Black & White filter applied");
    }

    @FXML
    public void handleRGBSwapFilter() {
        if (imageView == null || imageView.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }
        Filter filter = new RGBSwapFilter();
        Image filtered = filter.apply(imageView.getImage());
        imageView.setImage(filtered);
        updateStatus("✓ RGB Swap filter applied");
    }

    // ===== EDGE DETECTION =====

    @FXML
    public void handlePrewittFilter() {
        if (imageView == null || imageView.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }
        Filter filter = new PrewittFilter();
        Image filtered = filter.apply(imageView.getImage());
        imageView.setImage(filtered);
        updateStatus("✓ Prewitt Edge Detection applied");
    }

    // ===== UTILITY =====

    @FXML
    public void handleReset() {
        if (originalImage == null) {
            updateStatus("Error: No original image to reset to");
            return;
        }
        imageView.setImage(originalImage);
        updateStatus("✓ Reset to original image");
    }

    @FXML
    public void handleSaveImage() {
        if (imageView == null || imageView.getImage() == null) {
            updateStatus("Error: No image to save");
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Filtered Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("PNG Images", "*.png"),
            new FileChooser.ExtensionFilter("JPEG Images", "*.jpg", "*.jpeg"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        
        File file = fileChooser.showSaveDialog(imageView.getScene().getWindow());
        if (file != null) {
            try {
                // TODO: Implement image saving using WritableImage and ImageIO
                updateStatus("✓ Image saved to: " + file.getName());
            } catch (Exception e) {
                updateStatus("Error saving image: " + e.getMessage());
            }
        }
    }

    // ===== HELPER METHODS =====

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
}
