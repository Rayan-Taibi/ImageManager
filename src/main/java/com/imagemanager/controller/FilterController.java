package com.imagemanager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import com.imagemanager.model.filter.*;
import com.imagemanager.model.metadata.*;
import com.imagemanager.model.persistence.JsonMetadataDAO;
import java.util.function.Supplier;

public class FilterController {

    @FXML private ImageView imageView;
    @FXML private Label statusLabel, tagsLabel;
    @FXML private TextField passwordField, tagInputField;

    private Image originalImage;
    private String currentImagePath;
    private MetadataManager metadataManager;

    @FXML
    public void initialize() {
        metadataManager = new MetadataManager(new JsonMetadataDAO());
        updateStatus("No image loaded.");
    }

    // Generic helper to reduce 10 lines of code to 1 per filter
    private void applyFilter(String name, Supplier<Filter> filterSupplier) {
        if (imageView == null || imageView.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }
        Filter filter = filterSupplier.get();
        imageView.setImage(filter.apply(imageView.getImage()));
        recordAction(name, "filter");
        updateStatus("✓ " + name + " applied");
    }

    // ===== CONSOLIDATED COLOR & EDGE FILTERS =====
    @FXML public void handleSepiaFilter()      { applyFilter("Sepia", SepiaFilter::new); }
    @FXML public void handleNoirBlancFilter()  { applyFilter("NoireBlanc", NoireBlanc::new); }
    @FXML public void handleRGBSwapFilter()    { applyFilter("RGBSwap", RGBSwapFilter::new); }
    @FXML public void handlePrewittFilter()    { applyFilter("Prewitt", PrewittFilter::new); }

    // ===== SIMPLIFIED TRANSFORMATIONS =====
    @FXML public void RotationDroite()         { rotate(90, "RotationDroite"); }
    @FXML public void RotateGauche()           { rotate(-90, "RotateGauche"); }
    @FXML public void SymmetrieHorizontale()   { flip("X", "SymmetrieHorizontale"); }
    @FXML public void SymmetrieVerticale()     { flip("Y", "SymmetrieVerticale"); }

    private void rotate(double angle, String name) {
        if (imageView == null) return;
        imageView.setRotate(imageView.getRotate() + angle);
        recordAction(name, "transform");
    }

    private void flip(String axis, String name) {
        if (imageView == null) return;
        if (axis.equals("X")) imageView.setScaleX(imageView.getScaleX() * -1);
        else imageView.setScaleY(imageView.getScaleY() * -1);
        recordAction(name, "transform");
    }

    // ===== ENCRYPTION / DECRYPTION =====
    @FXML public void handleEncrypt() { processSecurity("Encryption", EncryptionFilter::new); }
    @FXML public void handleDecrypt() { processSecurity("Decryption", DecryptionFilter::new); }

    private void processSecurity(String name, java.util.function.Function<String, Filter> filterFactory) {
        String pass = passwordField.getText();
        if (pass == null || pass.isEmpty()) {
            updateStatus("Error: Enter password");
            return;
        }
        applyFilter(name, () -> filterFactory.apply(pass));
        passwordField.clear();
    }

    // ===== METADATA HELPERS =====
    private void recordAction(String name, String type) {
        if (currentImagePath != null) {
            metadataManager.addTransformation(currentImagePath, new Transformation(name, type));
        }
    }

    @FXML
    public void handleAddTag() {
        String tag = tagInputField.getText().trim();
        if (currentImagePath != null && !tag.isEmpty()) {
            metadataManager.addTag(currentImagePath, new Tag(tag));
            tagInputField.clear();
            loadTags();
            updateStatus("Tag added");
        }
    }

    @FXML
    public void loadTags() {
        if (currentImagePath == null) return;
        var tags = metadataManager.getTags(currentImagePath);
        String text = tags.isEmpty() ? "(none)" : String.join(", ", tags.stream().map(Tag::value).toList());
        tagsLabel.setText("Tags: " + text);
    }

    @FXML public void handleReset() {
        if (originalImage != null) {
            imageView.setImage(originalImage);
            updateStatus("Reset to original");
        }
    }

    @FXML public void handleSaveMetadata() { metadataManager.saveAll(); updateStatus("Saved"); }

    private void updateStatus(String msg) { if (statusLabel != null) statusLabel.setText(msg); }

    // Setters for MainController injection
    public void setImageView(ImageView iv) { this.imageView = iv; }
    public void setStatusLabel(Label sl) { this.statusLabel = sl; }
    public void setOriginalImage(Image img) { this.originalImage = img; }
    public void setImagePath(String path) { this.currentImagePath = path; loadTags(); }
}