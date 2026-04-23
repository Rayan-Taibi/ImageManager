package com.imagemanager.model.persistence;

import com.imagemanager.model.metadata.ImageMetadata;
import java.io.IOException;
import java.util.Map;

/**
 * Data Access Object interface for metadata persistence.
 * Defines contract for saving and loading image metadata.
 */
public interface MetadataDAO {
    //Save all metadata to storage.
     
    void saveMetadata(Map<String, ImageMetadata> allMetadata) throws IOException;

    // Load all metadata from storage.
     
    Map<String, ImageMetadata> loadMetadata() throws IOException;

    /**
     * Get metadata for a specific image.
     */
    ImageMetadata getMetadata(String imagePath) throws IOException;

    /**
     * Save metadata for a single image.
     */
    void saveMetadataForImage(String imagePath, ImageMetadata metadata) throws IOException;
}

