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
 * Controle la liste des images et la recherche par tag.
 */
public class LibraryController {
    @FXML
    private ListView<String> imageListView;

    @FXML
    private TextField searchField;

    @FXML
    private Label resultCountLabel;

    private MetadataManager gestionnaireMetadonnees;
    private MainController controleurPrincipal;
    private List<String> tousCheminsImages;

    private final ObservableList<String> cheminsAffiches = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        gestionnaireMetadonnees = new MetadataManager(new JsonMetadataDAO());

        // Nom du fichier a l'ecran, chemin complet en valeur.
        imageListView.setItems(cheminsAffiches);
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
            protected void updateItem(String chemin, boolean empty) {
                super.updateItem(chemin, empty);
                if (empty || chemin == null) {
                    setGraphic(null);
                } else {
                    nameLabel.setText(new File(chemin).getName());
                    setGraphic(container);
                }
            }
        });

        imageListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && controleurPrincipal != null) {
                controleurPrincipal.loadImage(newVal);
            }
        });

        loadImages();
    }

    /** Branche le controleur principal. */
    public void setMainController(MainController mainController) {
        this.controleurPrincipal = mainController;
    }

    /** Charge les images depuis les metadonnees. */
    private void loadImages() {
        tousCheminsImages = gestionnaireMetadonnees.getAllMetadata().keySet().stream().sorted().toList();
        displayImages(tousCheminsImages);
    }

    /** Met a jour la liste affichee. */
    private void displayImages(List<String> paths) {
        cheminsAffiches.setAll(paths);
        resultCountLabel.setText("Found: " + paths.size());
    }

    /** Filtre la liste avec le texte saisi. */
    @FXML
    public void handleSearch() {
        String termeRecherche = searchField.getText().trim().toLowerCase();
        
        if (termeRecherche.isEmpty()) {
            displayImages(tousCheminsImages);
            return;
        }

        List<String> cheminsFiltres = tousCheminsImages.stream()
            .filter(chemin -> {
                ImageMetadata metadonnees = gestionnaireMetadonnees.getMetadata(chemin);
                if (metadonnees == null) return false;
                return metadonnees.getTags().stream()
                    .anyMatch(tag -> tag.valeur().toLowerCase().contains(termeRecherche));
            })
            .toList();

        displayImages(cheminsFiltres);
    }

    /** Vide la recherche et remet toute la liste. */
    @FXML
    public void handleClearSearch() {
        searchField.clear();
        displayImages(tousCheminsImages);
    }

    public void refresh() {
        gestionnaireMetadonnees = new MetadataManager(new JsonMetadataDAO());
        loadImages();
    }

    /** Supprime l'image et ses metadonnees. */
    private void deleteImage(String cheminImage) {
        gestionnaireMetadonnees.clearMetadata(cheminImage);
        gestionnaireMetadonnees.saveAll();
        refresh();
    }
}

