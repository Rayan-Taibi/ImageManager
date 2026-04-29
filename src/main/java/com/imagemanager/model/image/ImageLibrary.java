package com.imagemanager.model.image;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Gere une collection d'images avec leurs metadonnees.
 *
 * Utilisé comme conteneur mémoire local pour l'application. Ne gère pas la
 * persistance (les métadonnées sont gérées séparément par `MetadataManager`).
 */
public class ImageLibrary {
    private final List<ImageWrapper> imagesDeLaBibliotheque = new ArrayList<>();

    /**
     * Ajoute une image a la bibliotheque.
     */
    public void addImage(ImageWrapper imageEmballee) {
        if (imageEmballee != null && !imagesDeLaBibliotheque.contains(imageEmballee)) {
            imagesDeLaBibliotheque.add(imageEmballee);
        }
    }

    /**
     * Supprime une image de la bibliotheque.
     */
    public void removeImage(ImageWrapper imageEmballee) {
        imagesDeLaBibliotheque.remove(imageEmballee);
    }

    /**
     * Retourne toutes les images de la bibliotheque.
     */
    public List<ImageWrapper> getAllImages() {
        return new ArrayList<>(imagesDeLaBibliotheque);
    }

    /**
     * Cherche une image par chemin.
     */
    public ImageWrapper findByPath(String chemin) {
        return imagesDeLaBibliotheque.stream()
            .filter(image -> image.getImagePath().equals(chemin))
            .findFirst()
            .orElse(null);
    }

    /**
     * Recherche des images par tag, avec correspondance partielle.
     */
    public List<ImageWrapper> searchByTag(String rechercheTag) {
        String rechercheEnMinuscules = rechercheTag.toLowerCase();
        return imagesDeLaBibliotheque.stream()
            .filter(image -> image.getMetadata().getTags().stream()
                .anyMatch(tag -> tag.valeur().toLowerCase().contains(rechercheEnMinuscules)))
            .collect(Collectors.toList());
    }

    /**
     * Retourne le nombre total d'images.
     */
    public int size() {
        return imagesDeLaBibliotheque.size();
    }

    /**
     * Vide la bibliotheque.
     */
    public void clear() {
        imagesDeLaBibliotheque.clear();
    }
}

