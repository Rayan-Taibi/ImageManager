package com.imagemanager.model.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagemanager.model.metadata.ImageMetadata;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Persistence des metadonnees au format JSON avec Jackson.
 * Stocke toutes les metadonnees dans un seul fichier JSON.
 */
public class JsonMetadataDAO implements MetadataDAO {
    private static final String FICHIER_METADONNEES = "metadata.json";
    private final ObjectMapper objectMapper;

    public JsonMetadataDAO() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void saveMetadata(Map<String, ImageMetadata> toutesLesMetadonnees) throws IOException {
        File fichier = new File(FICHIER_METADONNEES);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(fichier, toutesLesMetadonnees);
    }

    @Override
    public Map<String, ImageMetadata> loadMetadata() throws IOException {
        File fichier = new File(FICHIER_METADONNEES);
        if (!fichier.exists()) {
            return new HashMap<>();
        }
        return objectMapper.readValue(fichier,
            objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, ImageMetadata.class));
    }

    @Override
    public ImageMetadata getMetadata(String cheminImage) throws IOException {
        Map<String, ImageMetadata> toutesLesMetadonnees = loadMetadata();
        return toutesLesMetadonnees.getOrDefault(cheminImage, new ImageMetadata(cheminImage));
    }

    @Override
    public void saveMetadataForImage(String cheminImage, ImageMetadata metadonnees) throws IOException {
        Map<String, ImageMetadata> toutesLesMetadonnees = loadMetadata();
        toutesLesMetadonnees.put(cheminImage, metadonnees);
        saveMetadata(toutesLesMetadonnees);
    }
}
