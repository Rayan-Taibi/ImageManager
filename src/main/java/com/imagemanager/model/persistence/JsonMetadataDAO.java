package com.imagemanager.model.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagemanager.model.metadata.ImageMetadata;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON-based metadata persistence using Jackson.
 * Stores all metadata in a single JSON file.
 */
public class JsonMetadataDAO implements MetadataDAO {
    private static final String METADATA_FILE = "metadata.json";
    private final ObjectMapper objectMapper;

    public JsonMetadataDAO() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void saveMetadata(Map<String, ImageMetadata> allMetadata) throws IOException {
        File file = new File(METADATA_FILE);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, allMetadata);
    }

    @Override
    public Map<String, ImageMetadata> loadMetadata() throws IOException {
        File file = new File(METADATA_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }
        return objectMapper.readValue(file,
            objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, ImageMetadata.class));
    }

    @Override
    public ImageMetadata getMetadata(String imagePath) throws IOException {
        Map<String, ImageMetadata> allMetadata = loadMetadata();
        return allMetadata.getOrDefault(imagePath, new ImageMetadata(imagePath));
    }

    @Override
    public void saveMetadataForImage(String imagePath, ImageMetadata metadata) throws IOException {
        Map<String, ImageMetadata> allMetadata = loadMetadata();
        allMetadata.put(imagePath, metadata);
        saveMetadata(allMetadata);
    }
}
