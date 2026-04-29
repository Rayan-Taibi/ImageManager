package com.imagemanager.model.image;

import com.imagemanager.model.metadata.ImageMetadata;
import javafx.scene.image.Image;

/**
 * Conteneur d'image avec ses metadonnees associees.
 */
public class ImageWrapper {
    /**
     * Conteneur simple pour l'image en mémoire et son chemin + métadonnées.
     *
     * 
     * Remarque : le champ `cheminImage` est la référence utilisée pour
     * lier la métadonnée persistée (ImageMetadata) à cette image en mémoire.
     */
    private final Image image;
    private final String cheminImage;
    private final ImageMetadata metadata;

    public ImageWrapper(Image image, String cheminImage, ImageMetadata metadata) {
        this.image = image;
        this.cheminImage = cheminImage;
        this.metadata = metadata;
    }

    public Image getImage() {
        return image;
    }

    public String getImagePath() {
        return cheminImage;
    }

    public ImageMetadata getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "ImageWrapper{" +
            "imagePath='" + cheminImage + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
