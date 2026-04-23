package com.imagemanager.model.image;

import com.imagemanager.model.metadata.ImageMetadata;
import javafx.scene.image.Image;

/**
 * Wrapper for an Image with associated metadata.
 */
public class ImageWrapper {
    private final Image image;
    private final String imagePath;
    private final ImageMetadata metadata;

    public ImageWrapper(Image image, String imagePath, ImageMetadata metadata) {
        this.image = image;
        this.imagePath = imagePath;
        this.metadata = metadata;
    }

    public Image getImage() {
        return image;
    }

    public String getImagePath() {
        return imagePath;
    }

    public ImageMetadata getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "ImageWrapper{" +
                "imagePath='" + imagePath + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
