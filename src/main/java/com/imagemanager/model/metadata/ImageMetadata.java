package com.imagemanager.model.metadata;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Metadonnees d'une image: chemin, etiquettes et historique des transformations.
 *
 * Note : les annotations `@JsonProperty` garantissent la compatibilité du format
 * JSON (clé `imagePath`) avec les versions antérieures si le nom du champ a été
 * modifié dans le code.on doit  Eviter de changer la clé JSON si on eut rester
 * compatible avec les anciens fichiers `metadata.json`.
 */
public class ImageMetadata {
    @JsonProperty("imagePath")
    private String cheminImage;

    @JsonProperty("tags")
    private List<Tag> etiquettes = new ArrayList<>();

    @JsonProperty("transformations")
    private List<Transformation> transformations = new ArrayList<>();

    public ImageMetadata() {
    }

    public ImageMetadata(String cheminImage) {
        this.cheminImage = cheminImage;
    }

    public String getImagePath() {
        return cheminImage;
    }

    public void setImagePath(String cheminImage) {
        this.cheminImage = cheminImage;
    }

    public List<Tag> getTags() {
        return etiquettes;
    }

    public void setTags(List<Tag> etiquettes) {
        this.etiquettes = etiquettes != null ? etiquettes : new ArrayList<>();
    }

    public List<Transformation> getTransformations() {
        return transformations;
    }

    public void setTransformations(List<Transformation> transformations) {
        this.transformations = transformations != null ? transformations : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ImageMetadata{" +
            "imagePath='" + cheminImage + '\'' +
            ", tags=" + etiquettes +
                ", transformations=" + transformations +
                '}';
    }
}

