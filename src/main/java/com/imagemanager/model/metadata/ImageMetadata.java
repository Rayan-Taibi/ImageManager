package com.imagemanager.model.metadata;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Metadata for an image including path, tags, and transformation history.
 */
public class ImageMetadata {
    @JsonProperty("imagePath")
    private String imagePath;

    @JsonProperty("tags")
    private List<Tag> tags = new ArrayList<>();

    @JsonProperty("transformations")
    private List<Transformation> transformations = new ArrayList<>();

    public ImageMetadata() {
    }

    public ImageMetadata(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags != null ? tags : new ArrayList<>();
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
                "imagePath='" + imagePath + '\'' +
                ", tags=" + tags +
                ", transformations=" + transformations +
                '}';
    }
}

