package com.imagemanager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.imagemanager.model.filter.*;
import com.imagemanager.model.metadata.MetadataManager;
import com.imagemanager.model.metadata.Tag;
import com.imagemanager.model.metadata.Transformation;
import com.imagemanager.model.persistence.JsonMetadataDAO;

public class FilterController {

    @FXML
    private ImageView imageView;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField tagInputField;

    @FXML
    private Label tagsLabel;

    private Image originalImage;
    private String currentImagePath;
    private MetadataManager metadataManager;

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

    public void setImagePath(String imagePath) {
        this.currentImagePath = imagePath;
    }

    @FXML
    public void initialize() {
        // Initialize metadata manager with JSON persistence
        metadataManager = new MetadataManager(new JsonMetadataDAO());

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
        recordFilter("Sepia");
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
        recordFilter("NoireBlanc");
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
        recordFilter("RGBSwap");
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
        recordFilter("Prewitt");
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

    // ===== TRANSFORMATIONS =====

    @FXML
    public void RotationDroite() {
        if (imageView == null || imageView.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }

        imageView.setRotate(imageView.getRotate() + 90);
        recordTransformation("RotationDroite", "transform");
        updateStatus("✓ Rotation +90 applied");
    }

    @FXML
    public void RotateGauche() {
        if (imageView == null || imageView.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }

        imageView.setRotate(imageView.getRotate() - 90);
        recordTransformation("RotateGauche", "transform");
        updateStatus("✓ Rotation -90 applied");
    }

    @FXML
    public void SymmetrieHorizontale() {
        if (imageView == null || imageView.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }

        imageView.setScaleX(imageView.getScaleX() * -1);
        recordTransformation("SymmetrieHorizontale", "transform");
        updateStatus("✓ Horizontal mirror applied");
    }

    @FXML
    public void SymmetrieVerticale() {
        if (imageView == null || imageView.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }

        imageView.setScaleY(imageView.getScaleY() * -1);
        recordTransformation("SymmetrieVerticale", "transform");
        updateStatus("✓ Vertical mirror applied");
    }

    // ===== METADATA MANAGEMENT =====

    /**
     * Set current image path and load its metadata.
     */
    public void setCurrentImage(String imagePath) {
        this.currentImagePath = imagePath;
        loadTags();
    }

    /**
     * Record a transformation in metadata.
     */
    private void recordTransformation(String name, String type) {
        if (currentImagePath != null && metadataManager != null) {
            metadataManager.addTransformation(currentImagePath, new Transformation(name, type));
        }
    }

    /**
     * Record a filter application in metadata.
     */
    private void recordFilter(String filterName) {
        if (currentImagePath != null && metadataManager != null) {
            metadataManager.addTransformation(currentImagePath, new Transformation(filterName, "filter"));
        }
    }

    /**
     * Load and display tags for current image.
     */
    @FXML
    public void loadTags() {
        if (currentImagePath == null || metadataManager == null) {
            return;
        }

        var tags = metadataManager.getTags(currentImagePath);
        StringBuilder tagText = new StringBuilder("Tags: ");
        if (tags.isEmpty()) {
            tagText.append("(none)");
        } else {
            tagText.append(String.join(", ", tags.stream().map(Tag::value).toList()));
        }

        if (tagsLabel != null) {
            tagsLabel.setText(tagText.toString());
        }
    }

    /**
     * Load transformations from metadata and apply them in order.
     * This ensures that when an image is loaded, all previously applied filters are reapplied.
     */
    public void loadAndApplyTransformations(String imagePath) {
        if (metadataManager == null || imageView == null || imageView.getImage() == null) {
            return;
        }

        var transformations = metadataManager.getTransformations(imagePath);
        if (transformations.isEmpty()) {
            return;
        }

        Image currentImage = imageView.getImage();
        
        // Apply each transformation in order
        for (Transformation t : transformations) {
            String transformationName = t.name();
            
            if ("filter".equals(t.type())) {
                // Apply filter transformations
                currentImage = applyFilterByName(transformationName, currentImage);
            } else if ("transform".equals(t.type())) {
                // Apply UI transformations (rotation, mirror)
                applyUITransformation(transformationName);
            }
        }
        
        imageView.setImage(currentImage);
        updateStatus("✓ Transformations loaded and applied");
    }

    /**
     * Apply a filter by name.
     */
    private Image applyFilterByName(String filterName, Image image) {
        Filter filter = null;
        
        switch (filterName) {
            case "Sepia":
                filter = new SepiaFilter();
                break;
            case "NoireBlanc":
                filter = new NoireBlanc();
                break;
            case "RGBSwap":
                filter = new RGBSwapFilter();
                break;
            case "Prewitt":
                filter = new PrewittFilter();
                break;
            case "Encryption":
                // Cannot reapply encryption without password
                return image;
            case "Decryption":
                // Cannot reapply decryption without password
                return image;
            default:
                return image;
        }
        
        if (filter != null) {
            return filter.apply(image);
        }
        return image;
    }

    /**
     * Apply UI transformations (rotation, mirror).
     */
    private void applyUITransformation(String transformationName) {
        if (imageView == null) {
            return;
        }
        
        switch (transformationName) {
            case "RotationDroite":
                imageView.setRotate(imageView.getRotate() + 90);
                break;
            case "RotateGauche":
                imageView.setRotate(imageView.getRotate() - 90);
                break;
            case "SymmetrieHorizontale":
                imageView.setScaleX(imageView.getScaleX() * -1);
                break;
            case "SymmetrieVerticale":
                imageView.setScaleY(imageView.getScaleY() * -1);
                break;
        }
    }

    /**
     * Add a new tag to current image.
     */
    @FXML
    public void handleAddTag() {
        if (currentImagePath == null || metadataManager == null) {
            updateStatus("Error: No image loaded");
            return;
        }

        if (tagInputField == null || tagInputField.getText().trim().isEmpty()) {
            updateStatus("Error: Tag input is empty");
            return;
        }

        String tagValue = tagInputField.getText().trim();
        metadataManager.addTag(currentImagePath, new Tag(tagValue));
        tagInputField.clear();
        loadTags();
        updateStatus("✓ Tag added: " + tagValue);
    }

    /**
     * Save all metadata to file.
     */
    @FXML
    public void handleSaveMetadata() {
        if (metadataManager == null) {
            updateStatus("Error: Metadata manager not initialized");
            return;
        }

        metadataManager.saveAll();
        updateStatus("✓ Metadata saved");
    }

    /**
     * Encrypt the current image with a password.
     */
    @FXML
    public void handleEncrypt() {
        if (imageView == null || imageView.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }

        if (passwordField == null || passwordField.getText().isEmpty()) {
            updateStatus("Error: Enter a password");
            return;
        }

        String password = passwordField.getText();
        Filter encryptionFilter = new EncryptionFilter(password);
        Image encrypted = encryptionFilter.apply(imageView.getImage());
        imageView.setImage(encrypted);

        // Record encryption without storing password
        recordFilter("Encryption");
        passwordField.clear();
        updateStatus("✓ Image encrypted");
    }

    /**
     * Decrypt the current image with a password.
     */
    @FXML
    public void handleDecrypt() {
        if (imageView == null || imageView.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }

        if (passwordField == null || passwordField.getText().isEmpty()) {
            updateStatus("Error: Enter a password");
            return;
        }

        String password = passwordField.getText();
        Filter decryptionFilter = new DecryptionFilter(password);
        Image decrypted = decryptionFilter.apply(imageView.getImage());
        imageView.setImage(decrypted);

        // Record decryption
        recordFilter("Decryption");
        passwordField.clear();
        updateStatus("✓ Image decrypted");
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
}
