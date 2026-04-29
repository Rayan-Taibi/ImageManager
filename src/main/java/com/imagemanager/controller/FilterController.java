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

    // Champs relies a filter.fxml
    @FXML private Label panelStatusLabel;
    @FXML private Label tagsLabel;
    @FXML private TextField passwordField;
    @FXML private TextField tagInputField;

    // Champs relies au controleur principal
    private ImageView vueImage;
    private Label labelStatutPrincipal;
    private LibraryController controleurBibliotheque;

    private Image imageOriginale;
    private String cheminImageCourant;
    private MetadataManager gestionnaireMetadonnees;

    private boolean relectureEnCours = false;

    @FXML
    public void initialize() {
        gestionnaireMetadonnees = new MetadataManager(new JsonMetadataDAO());
        updateStatus("No image loaded.");
    }

    // ---- API partagee ----

    public void setImageView(ImageView iv) {
        this.vueImage = iv;
    }

    public void setLibraryController(LibraryController libraryController) {
        this.controleurBibliotheque = libraryController;
    }

    /** Met a jour l'etat principal. */
    public void setMainStatusLabel(Label label) {
        this.labelStatutPrincipal = label;
    }

    public void loadImageFromPath(String cheminAbsolu) {
        if (cheminAbsolu == null || cheminAbsolu.isBlank()) {
            updateStatus("Error: invalid path");
            return;
        }

        File fichier = new File(cheminAbsolu);
        if (!fichier.exists()) {
            updateStatus("Error: file not found");
            return;
        }

        Image imageChargee = new Image(fichier.toURI().toString());
        this.imageOriginale = imageChargee;
        this.cheminImageCourant = cheminAbsolu;

        if (vueImage != null) {
            vueImage.setImage(imageChargee);
            resetViewTransforms();
        }

        loadAndApplyTransformations(cheminAbsolu);
        loadTags();

        updateStatus("Loaded: " + fichier.getName());
    }

    public void loadAndApplyTransformations(String cheminImage) {
        if (cheminImage == null || imageOriginale == null || vueImage == null) return;

        List<Transformation> transformations = gestionnaireMetadonnees.getTransformations(cheminImage);
        if (transformations == null || transformations.isEmpty()) return;

        relectureEnCours = true;
        try {
            vueImage.setImage(imageOriginale);
            resetViewTransforms();

            for (Transformation transformation : transformations) {
                if (transformation == null) continue;

                // Pas de relecture pour la securite.
                if ("security".equalsIgnoreCase(transformation.typeTransformation())) {
                    continue;
                }

                if ("transform".equalsIgnoreCase(transformation.typeTransformation())) {
                    applyUITransformation(transformation.nom());
                } else {
                    // Sinon, on tente un filtre.
                    applyFilterByName(transformation.nom());
                }
            }
        } finally {
            relectureEnCours = false;
        }
    }

    // ---- Filtres ----

    private void applyFilter(String nom, Supplier<Filter> fournisseurFiltre) {
        if (vueImage == null || vueImage.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }
        Filter filtre = fournisseurFiltre.get();
        vueImage.setImage(filtre.apply(vueImage.getImage()));
        recordAction(nom, "filter");
        updateStatus("✓ " + nom + " applied");
    }

    private void applyFilterByName(String nom) {
        if (nom == null) return;
        switch (nom) {
            case "Sepia" -> applyFilter("Sepia", SepiaFilter::new);
            case "NoireBlanc" -> applyFilter("NoireBlanc", NoireBlanc::new);
            case "RGBSwap" -> applyFilter("RGBSwap", RGBSwapFilter::new);
            case "Prewitt" -> applyFilter("Prewitt", PrewittFilter::new);
            default -> { /* ignore inconnu */ }
        }
    }

    @FXML public void handleSepiaFilter() { applyFilter("Sepia", SepiaFilter::new); }
    @FXML public void handleNoirBlancFilter() { applyFilter("NoireBlanc", NoireBlanc::new); }
    @FXML public void handleRGBSwapFilter() { applyFilter("RGBSwap", RGBSwapFilter::new); }
    @FXML public void handlePrewittFilter() { applyFilter("Prewitt", PrewittFilter::new); }

    // ---- Transform UI ----

    @FXML public void RotationDroite() { rotate(90, "RotationDroite"); }
    @FXML public void RotateGauche() { rotate(-90, "RotateGauche"); }
    @FXML public void SymmetrieHorizontale() { flipX("SymmetrieHorizontale"); }
    @FXML public void SymmetrieVerticale() { flipY("SymmetrieVerticale"); }

    private void applyUITransformation(String nom) {
        if (nom == null) return;
        switch (nom) {
            case "RotationDroite" -> rotate(90, "RotationDroite");
            case "RotateGauche" -> rotate(-90, "RotateGauche");
            case "SymmetrieHorizontale" -> flipX("SymmetrieHorizontale");
            case "SymmetrieVerticale" -> flipY("SymmetrieVerticale");
            default -> { /* ignore inconnu */ }
        }
    }

    private void rotate(double angle, String nom) {
        if (vueImage == null) return;
        vueImage.setRotate(vueImage.getRotate() + angle);
        recordAction(nom, "transform");
    }

    private void flipX(String nom) {
        if (vueImage == null) return;
        vueImage.setScaleX(vueImage.getScaleX() * -1);
        recordAction(nom, "transform");
    }

    private void flipY(String nom) {
        if (vueImage == null) return;
        vueImage.setScaleY(vueImage.getScaleY() * -1);
        recordAction(nom, "transform");
    }

    private void resetViewTransforms() {
        if (vueImage == null) return;
        vueImage.setRotate(0);
        vueImage.setScaleX(1);
        vueImage.setScaleY(1);
    }

    // ---- Securite ----

    @FXML
    public void handleEncrypt() {
        processSecurity("Encryption", EncryptionFilter::new);
    }

    @FXML
    public void handleDecrypt() {
        processSecurity("Decryption", DecryptionFilter::new);
    }

    private void processSecurity(String nom, java.util.function.Function<String, Filter> usineFiltre) {
        if (vueImage == null || vueImage.getImage() == null) {
            updateStatus("Error: No image loaded");
            return;
        }

        String motDePasse = passwordField != null ? passwordField.getText() : null;
        if (motDePasse == null || motDePasse.isBlank()) {
            updateStatus("Error: Enter password");
            return;
        }

        Filter filtre = usineFiltre.apply(motDePasse);
        vueImage.setImage(filtre.apply(vueImage.getImage()));
        recordAction(nom, "security");
        updateStatus("✓ " + nom + " applied");

        if (passwordField != null) passwordField.clear();
    }

    // ---- Tags / meta ----

    private void recordAction(String nom, String typeTransformation) {
        if (relectureEnCours) return;
        if (cheminImageCourant != null) {
            gestionnaireMetadonnees.addTransformation(cheminImageCourant, new Transformation(nom, typeTransformation));
        }
    }

    @FXML
    public void handleAddTag() {
        if (tagInputField == null) return;
        String etiquette = tagInputField.getText() != null ? tagInputField.getText().trim() : "";

        if (cheminImageCourant != null && !etiquette.isEmpty()) {
            gestionnaireMetadonnees.addTag(cheminImageCourant, new Tag(etiquette));
            tagInputField.clear();
            loadTags();
            updateStatus("Tag added");
        }
    }

    @FXML
    public void loadTags() {
        if (cheminImageCourant == null || tagsLabel == null) return;
        var etiquettes = gestionnaireMetadonnees.getTags(cheminImageCourant);
        String texte = etiquettes.isEmpty() ? "(none)" : String.join(", ", etiquettes.stream().map(Tag::valeur).toList());
        tagsLabel.setText("Tags: " + texte);
    }

    @FXML
    public void handleReset() {
        if (imageOriginale != null && vueImage != null) {
            vueImage.setImage(imageOriginale);
            resetViewTransforms();
            updateStatus("Reset to original");
        }
    }

    @FXML
    public void handleSaveMetadata() {
        gestionnaireMetadonnees.saveAll();
        updateStatus("Saved");
        if (controleurBibliotheque != null) {
            controleurBibliotheque.refresh();
        }
    }

    private void updateStatus(String message) {
        if (panelStatusLabel != null) panelStatusLabel.setText(message);
        if (labelStatutPrincipal != null) labelStatutPrincipal.setText(message);
    }
}
