package com.imagemanager.model.persistence;

import com.imagemanager.model.metadata.ImageMetadata;
import java.io.IOException;
import java.util.Map;

/**
 * DAO des metadonnees.
 */
public interface MetadataDAO {
    void saveMetadata(Map<String, ImageMetadata> toutesLesMetadonnees) throws IOException;

    Map<String, ImageMetadata> loadMetadata() throws IOException;

    /** Retourne les metadonnees d'une image. */
    ImageMetadata getMetadata(String cheminImage) throws IOException;

    /** Sauve les metadonnees d'une image. */
    void saveMetadataForImage(String cheminImage, ImageMetadata metadonnees) throws IOException;
}

