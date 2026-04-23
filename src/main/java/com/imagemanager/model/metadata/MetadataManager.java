package com.imagemanager.model.metadata;

import com.imagemanager.model.persistence.MetadataDAO;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages metadata operations (CRUD) for images.
 * Coordinates with MetadataDAO for persistence.
 */
public class MetadataManager {
    private final MetadataDAO dao;
    private Map<String, ImageMetadata> metadata;

    public MetadataManager(MetadataDAO dao) {
        this.dao = dao;
        this.metadata = new HashMap<>();
        loadAll();
    }

    /**
     * Load all metadata from storage.
     */
    private void loadAll() {
        try {
            metadata = dao.loadMetadata();
        } catch (IOException e) {
            System.err.println("Error loading metadata: " + e.getMessage());
            metadata = new HashMap<>();
        }
    }

    /**
     * Get or create metadata for an image path.
     */
    private ImageMetadata getOrCreateMetadata(String imagePath) {
        return metadata.computeIfAbsent(imagePath, path -> new ImageMetadata(path));
    }

    /**
     * Add a transformation to an image's metadata.
     */
    public void addTransformation(String imagePath, Transformation transformation) {
        if (imagePath == null || transformation == null) return;
        ImageMetadata meta = getOrCreateMetadata(imagePath);
        meta.getTransformations().add(transformation);
    }

    /**
     * Add a tag to an image's metadata.
     */
    public void addTag(String imagePath, Tag tag) {
        if (imagePath == null || tag == null) return;
        ImageMetadata meta = getOrCreateMetadata(imagePath);
        if (!meta.getTags().contains(tag)) {
            meta.getTags().add(tag);
        }
    }

    /**
     * Get all tags for an image.
     */
    public List<Tag> getTags(String imagePath) {
        if (imagePath == null) return List.of();
        ImageMetadata meta = metadata.get(imagePath);
        return meta != null ? meta.getTags() : List.of();
    }

    /**
     * Get all transformations for an image.
     */
    public List<Transformation> getTransformations(String imagePath) {
        if (imagePath == null) return List.of();
        ImageMetadata meta = metadata.get(imagePath);
        return meta != null ? meta.getTransformations() : List.of();
    }

    /**
     * Save all metadata to storage.
     */
    public void saveAll() {
        try {
            dao.saveMetadata(metadata);
        } catch (IOException e) {
            System.err.println("Error saving metadata: " + e.getMessage());
        }
    }

    /**
     * Save metadata for a single image.
     */
    public void save(String imagePath) {
        if (imagePath == null) return;
        ImageMetadata meta = metadata.get(imagePath);
        if (meta != null) {
            try {
                dao.saveMetadataForImage(imagePath, meta);
            } catch (IOException e) {
                System.err.println("Error saving metadata: " + e.getMessage());
            }
        }
    }

    /**
     * Get metadata for an image.
     */
    public ImageMetadata getMetadata(String imagePath) {
        if (imagePath == null) return null;
        return metadata.get(imagePath);
    }

    /**
     * Get all metadata.
     */
    public Map<String, ImageMetadata> getAllMetadata() {
        return new HashMap<>(metadata);
    }

    /**
     * Clear a specific image's metadata.
     */
    public void clearMetadata(String imagePath) {
        if (imagePath != null) {
            metadata.remove(imagePath);
        }
    }

    /**
     * Clear all metadata.
     */
    public void clearAll() {
        metadata.clear();
    }
}

