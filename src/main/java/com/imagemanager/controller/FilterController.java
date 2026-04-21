package com.imagemanager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.imagemanager.model.filter.*;

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
    public void RotationDroite() {
        if (imageView == null || imageView.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }

        imageView.setRotate(imageView.getRotate() + 90);
        updateStatus("✓ Rotation +90 applied");
    }

    @FXML
    public void RotateGauche() {
        if (imageView == null || imageView.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }

        imageView.setRotate(imageView.getRotate() - 90);
        updateStatus("✓ Rotation -90 applied");
    }

    @FXML
    public void SymmetrieHorizontale() {
        if (imageView == null || imageView.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }

        imageView.setScaleX(imageView.getScaleX() * -1);
        updateStatus("✓ Horizontal mirror applied");
    }

    @FXML
    public void SymmetrieVerticale() {
        if (imageView == null || imageView.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }

        imageView.setScaleY(imageView.getScaleY() * -1);
        updateStatus("✓ Vertical mirror applied");
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
}
