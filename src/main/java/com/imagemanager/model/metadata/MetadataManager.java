package com.imagemanager.model.metadata;

import com.imagemanager.model.persistence.MetadataDAO;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestionnaire de métadonnées des images.
 *
 * Ce composant sert de cache en mémoire et de façade vers un `MetadataDAO` pour
 * la lecture/écriture. Il fournit des méthodes pratiques pour :
 * - ajouter/consulter des tags et transformations,
 * - sauvegarder soit une fiche, soit l'ensemble des métadonnées.
 *
 * Implémentation :
 * - charge tout au démarrage via `loadAll()` puis conserve un `Map` en mémoire.
 * - les opérations mutantes modifient ce cache ; il faut appeler `saveAll()` ou
 *   `save()` pour persister selon le besoin.
 */
public class MetadataManager {
    private final MetadataDAO sourceDeDonnees;
    private Map<String, ImageMetadata> metadonnees;

    public MetadataManager(MetadataDAO sourceDeDonnees) {
        this.sourceDeDonnees = sourceDeDonnees;
        this.metadonnees = new HashMap<>();
        loadAll();
    }



    /** Charge tout depuis le stockage. */
    private void loadAll() {
        try {
            metadonnees = sourceDeDonnees.loadMetadata();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des metadonnees: " + e.getMessage());
            metadonnees = new HashMap<>();
        }
    }

    /** Recupere ou cree la fiche image. */
    private ImageMetadata getOrCreateMetadata(String cheminImage) {
        return metadonnees.computeIfAbsent(cheminImage, chemin -> new ImageMetadata(chemin));
    }

    /** Ajoute une transformation. */
    public void addTransformation(String cheminImage, Transformation transformation) {
        if (cheminImage == null || transformation == null) return;
        ImageMetadata metadonneesImage = getOrCreateMetadata(cheminImage);
        metadonneesImage.getTransformations().add(transformation);
    }

    /** Ajoute un tag. */
    public void addTag(String cheminImage, Tag tag) {
        if (cheminImage == null || tag == null) return;
        ImageMetadata metadonneesImage = getOrCreateMetadata(cheminImage);
        if (!metadonneesImage.getTags().contains(tag)) {
            metadonneesImage.getTags().add(tag);
        }
    }

    /** Retourne les tags d'une image. */
    public List<Tag> getTags(String cheminImage) {
        if (cheminImage == null) return List.of();
        ImageMetadata metadonneesImage = metadonnees.get(cheminImage);
        return metadonneesImage != null ? metadonneesImage.getTags() : List.of();
    }

    /** Retourne les transformations d'une image. */
    public List<Transformation> getTransformations(String cheminImage) {
        if (cheminImage == null) return List.of();
        ImageMetadata metadonneesImage = metadonnees.get(cheminImage);
        return metadonneesImage != null ? metadonneesImage.getTransformations() : List.of();
    }

    /** Sauveg tout. */
    public void saveAll() {
        try {
            sourceDeDonnees.saveMetadata(metadonnees);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des metadonnees: " + e.getMessage());
        }
    }

    /** Sauveg une seule image. */
    public void save(String cheminImage) {
        if (cheminImage == null) return;
        ImageMetadata metadonneesImage = metadonnees.get(cheminImage);
        if (metadonneesImage != null) {
            try {
                sourceDeDonnees.saveMetadataForImage(cheminImage, metadonneesImage);
            } catch (IOException e) {
                System.err.println("Erreur lors de la sauvegarde des metadonnees: " + e.getMessage());
            }
        }
    }

    /** Retourne la fiche image. */
    public ImageMetadata getMetadata(String cheminImage) {
        if (cheminImage == null) return null;
        return metadonnees.get(cheminImage);
    }

    /** Retourne tout le cache. */
    public Map<String, ImageMetadata> getAllMetadata() {
        return new HashMap<>(metadonnees);
    }

    /** Supprime la fiche image. */
    public void clearMetadata(String cheminImage) {
        if (cheminImage != null) {
            metadonnees.remove(cheminImage);
        }
    }

    /** Vide le cache. */
    public void clearAll() {
        metadonnees.clear();
    }
}

