package com.imagemanager.model.image;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages a collection of wrapped images with metadata.
 */
public class ImageLibrary {
    private final List<ImageWrapper> images = new ArrayList<>();

    /**
     * Add an image to the library.
     */
    public void addImage(ImageWrapper wrapper) {
        if (wrapper != null && !images.contains(wrapper)) {
            images.add(wrapper);
        }
    }

    /**
     * Remove an image from the library.
     */
    public void removeImage(ImageWrapper wrapper) {
        images.remove(wrapper);
    }

    /**
     * Get all images in the library.
     */
    public List<ImageWrapper> getAllImages() {
        return new ArrayList<>(images);
    }

    /**
     * Find image by path.
     */
    public ImageWrapper findByPath(String path) {
        return images.stream()
            .filter(img -> img.getImagePath().equals(path))
            .findFirst()
            .orElse(null);
    }

    /**
     * Search images by tag (partial matching).
     */
    public List<ImageWrapper> searchByTag(String tagSearch) {
        String searchLower = tagSearch.toLowerCase();
        return images.stream()
            .filter(img -> img.getMetadata().getTags().stream()
                .anyMatch(tag -> tag.value().toLowerCase().contains(searchLower)))
            .collect(Collectors.toList());
    }

    /**
     * Get total number of images in library.
     */
    public int size() {
        return images.size();
    }

    /**
     * Clear all images from library.
     */
    public void clear() {
        images.clear();
    }
}

