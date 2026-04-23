package com.imagemanager.controller;

import com.imagemanager.model.filter.*;
import com.imagemanager.model.metadata.MetadataManager;
import com.imagemanager.model.metadata.Tag;
import com.imagemanager.model.metadata.Transformation;
import com.imagemanager.model.persistence.JsonMetadataDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.List;
import java.util.function.Supplier;

public class FilterController {

    // Injected from filter.fxml
    @FXML private Label panelStatusLabel;
    @FXML private Label tagsLabel;
    @FXML private TextField passwordField;
    @FXML private TextField tagInputField;

    // Injected from MainController
    private ImageView imageView;
    private Label mainStatusLabel;
    private LibraryController libraryController;

    private Image originalImage;
    private String currentImagePath;
    private MetadataManager metadataManager;

    private boolean replaying = false;

    @FXML
    public void initialize() {
        metadataManager = new MetadataManager(new JsonMetadataDAO());
        updateStatus("No image loaded.");
    }

    // ---- Public API used by MainController / LibraryController ----

    public void setImageView(ImageView iv) {
        this.imageView = iv;
    }

    public void setLibraryController(LibraryController libraryController) {
        this.libraryController = libraryController;
    }

    /** Updates the top status bar in main.fxml (labelStatut). */
    public void setMainStatusLabel(Label label) {
        this.mainStatusLabel = label;
    }

    public void loadImageFromPath(String absolutePath) {
        if (absolutePath == null || absolutePath.isBlank()) {
            updateStatus("Error: invalid path");
            return;
        }

        File file = new File(absolutePath);
        if (!file.exists()) {
            updateStatus("Error: file not found");
            return;
        }

        Image image = new Image(file.toURI().toString());
        this.originalImage = image;
        this.currentImagePath = absolutePath;

        if (imageView != null) {
            imageView.setImage(image);
            resetViewTransforms();
        }

        loadAndApplyTransformations(absolutePath);
        loadTags();

        updateStatus("Loaded: " + file.getName());
    }

    public void loadAndApplyTransformations(String imagePath) {
        if (imagePath == null || originalImage == null || imageView == null) return;

        List<Transformation> transformations = metadataManager.getTransformations(imagePath);
        if (transformations == null || transformations.isEmpty()) return;

        replaying = true;
        try {
            imageView.setImage(originalImage);
            resetViewTransforms();

            for (Transformation t : transformations) {
                if (t == null) continue;

                // Security transforms can’t be replayed (password is NOT stored by design)
                if ("security".equalsIgnoreCase(t.type())) {
                    continue;
                }

                if ("transform".equalsIgnoreCase(t.type())) {
                    applyUITransformation(t.name());
                } else {
                    // Default to filter
                    applyFilterByName(t.name());
                }
            }
        } finally {
            replaying = false;
        }
    }

    // ---- Filters (recorded as type=filter) ----

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

    private void applyFilterByName(String name) {
        if (name == null) return;
        switch (name) {
            case "Sepia" -> applyFilter("Sepia", SepiaFilter::new);
            case "NoireBlanc" -> applyFilter("NoireBlanc", NoireBlanc::new);
            case "RGBSwap" -> applyFilter("RGBSwap", RGBSwapFilter::new);
            case "Prewitt" -> applyFilter("Prewitt", PrewittFilter::new);
            default -> { /* ignore unknown */ }
        }
    }

    @FXML public void handleSepiaFilter() { applyFilter("Sepia", SepiaFilter::new); }
    @FXML public void handleNoirBlancFilter() { applyFilter("NoireBlanc", NoireBlanc::new); }
    @FXML public void handleRGBSwapFilter() { applyFilter("RGBSwap", RGBSwapFilter::new); }
    @FXML public void handlePrewittFilter() { applyFilter("Prewitt", PrewittFilter::new); }

    // ---- UI transforms (recorded as type=transform) ----

    @FXML public void RotationDroite() { rotate(90, "RotationDroite"); }
    @FXML public void RotateGauche() { rotate(-90, "RotateGauche"); }
    @FXML public void SymmetrieHorizontale() { flipX("SymmetrieHorizontale"); }
    @FXML public void SymmetrieVerticale() { flipY("SymmetrieVerticale"); }

    private void applyUITransformation(String name) {
        if (name == null) return;
        switch (name) {
            case "RotationDroite" -> rotate(90, "RotationDroite");
            case "RotateGauche" -> rotate(-90, "RotateGauche");
            case "SymmetrieHorizontale" -> flipX("SymmetrieHorizontale");
            case "SymmetrieVerticale" -> flipY("SymmetrieVerticale");
            default -> { /* ignore unknown */ }
        }
    }

    private void rotate(double angle, String name) {
        if (imageView == null) return;
        imageView.setRotate(imageView.getRotate() + angle);
        recordAction(name, "transform");
    }

    private void flipX(String name) {
        if (imageView == null) return;
        imageView.setScaleX(imageView.getScaleX() * -1);
        recordAction(name, "transform");
    }

    private void flipY(String name) {
        if (imageView == null) return;
        imageView.setScaleY(imageView.getScaleY() * -1);
        recordAction(name, "transform");
    }

    private void resetViewTransforms() {
        if (imageView == null) return;
        imageView.setRotate(0);
        imageView.setScaleX(1);
        imageView.setScaleY(1);
    }

    // ---- Security (NOT replayable) ----

    @FXML
    public void handleEncrypt() {
        processSecurity("Encryption", EncryptionFilter::new);
    }

    @FXML
    public void handleDecrypt() {
        processSecurity("Decryption", DecryptionFilter::new);
    }

    private void processSecurity(String name, java.util.function.Function<String, Filter> filterFactory) {
        if (imageView == null || imageView.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }

        String pass = passwordField != null ? passwordField.getText() : null;
        if (pass == null || pass.isBlank()) {
            updateStatus("Error: Enter password");
            return;
        }

        Filter filter = filterFactory.apply(pass);
        imageView.setImage(filter.apply(imageView.getImage()));
        recordAction(name, "security");
        updateStatus("✓ " + name + " applied");

        if (passwordField != null) passwordField.clear();
    }

    // ---- Tags / metadata ----

    private void recordAction(String name, String type) {
        if (replaying) return;
        if (currentImagePath != null) {
            metadataManager.addTransformation(currentImagePath, new Transformation(name, type));
        }
    }

    @FXML
    public void handleAddTag() {
        if (tagInputField == null) return;
        String tag = tagInputField.getText() != null ? tagInputField.getText().trim() : "";

        if (currentImagePath != null && !tag.isEmpty()) {
            metadataManager.addTag(currentImagePath, new Tag(tag));
            tagInputField.clear();
            loadTags();
            updateStatus("Tag added");
        }
    }

    @FXML
    public void loadTags() {
        if (currentImagePath == null || tagsLabel == null) return;
        var tags = metadataManager.getTags(currentImagePath);
        String text = tags.isEmpty() ? "(none)" : String.join(", ", tags.stream().map(Tag::value).toList());
        tagsLabel.setText("Tags: " + text);
    }

    @FXML
    public void handleReset() {
        if (originalImage != null && imageView != null) {
            imageView.setImage(originalImage);
            resetViewTransforms();
            updateStatus("Reset to original");
        }
    }

    @FXML
    public void handleSaveMetadata() {
        metadataManager.saveAll();
        updateStatus("Saved");
        if (libraryController != null) {
            libraryController.refresh();
        }
    }

    private void updateStatus(String msg) {
        if (panelStatusLabel != null) panelStatusLabel.setText(msg);
        if (mainStatusLabel != null) mainStatusLabel.setText(msg);
    }
}
