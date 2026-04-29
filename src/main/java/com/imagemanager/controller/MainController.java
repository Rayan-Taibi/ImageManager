package com.imagemanager.controller;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class MainController {
    /**
     * Contrôleur principal de la fenêtre.
     *
     * Rôle :
     * - Agir comme point d'entrée UI pour ouvrir des images.
     * - Connecter les sous-contrôleurs (`FilterController`, `LibraryController`).
     * - Déléguer le chargement d'images et exposer quelques raccourcis UI.
     *
     * Notes : les méthodes publiques sont utilisées depuis le FXML et par les autres
     * contrôleurs, garder les signatures stables évite de casser les liaisons.
     */
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
        FileChooser selecteurFichier = new FileChooser();
        selecteurFichier.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File fichierSelectionne = selecteurFichier.showOpenDialog(imageView.getScene().getWindow());
        if (fichierSelectionne == null) {
            return;
        }

        if (filterController != null) {
            filterController.loadImageFromPath(fichierSelectionne.getAbsolutePath());
        }

        labelStatut.setText("Image chargée : " + fichierSelectionne.getName());
    }
    public void loadImage(String cheminImage) {
        if (filterController != null) {
            filterController.loadImageFromPath(cheminImage);
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

