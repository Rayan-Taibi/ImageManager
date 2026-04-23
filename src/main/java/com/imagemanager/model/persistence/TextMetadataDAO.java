package com.imagemanager.model.persistence;

import com.imagemanager.model.metadata.ImageMetadata;
import com.imagemanager.model.metadata.Tag;
import com.imagemanager.model.metadata.Transformation;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Text-based metadata persistence.
 * Stores metadata in a simple, human-readable text format.
 */
public class TextMetadataDAO implements MetadataDAO {
    private static final String METADATA_FILE = "metadata.txt";

    @Override
    public void saveMetadata(Map<String, ImageMetadata> allMetadata) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(METADATA_FILE))) {
            for (ImageMetadata metadata : allMetadata.values()) {
                writer.write("IMAGE: " + metadata.getImagePath());
                writer.newLine();

                // Write tags
                writer.write("TAGS: ");
                for (Tag tag : metadata.getTags()) {
                    writer.write(tag.value());
                    writer.write("|");
                }
                writer.newLine();

                // Write transformations
                for (Transformation t : metadata.getTransformations()) {
                    writer.write("TRANSFORM: " + t.name() + " " + t.type());
                    writer.newLine();
                }

                writer.write("---");
                writer.newLine();
            }
        }
    }

    @Override
    public Map<String, ImageMetadata> loadMetadata() throws IOException {
        Map<String, ImageMetadata> result = new HashMap<>();
        File file = new File(METADATA_FILE);
        if (!file.exists()) {
            return result;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(METADATA_FILE))) {
            String line;
            ImageMetadata current = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("IMAGE: ")) {
                    String path = line.substring(7);
                    current = new ImageMetadata(path);
                    result.put(path, current);
                } else if (line.startsWith("TAGS: ") && current != null) {
                    String tags = line.substring(6);
                    if (!tags.isEmpty()) {
                        for (String tag : tags.split("\\|")) {
                            if (!tag.isEmpty()) {
                                current.getTags().add(new Tag(tag));
                            }
                        }
                    }
                } else if (line.startsWith("TRANSFORM: ") && current != null) {
                    String[] parts = line.substring(11).split(" ", 2);
                    if (parts.length >= 2) {
                        current.getTransformations().add(new Transformation(parts[0], parts[1]));
                    }
                }
            }
        }
        return result;
    }

    @Override
    public ImageMetadata getMetadata(String imagePath) throws IOException {
        Map<String, ImageMetadata> all = loadMetadata();
        return all.getOrDefault(imagePath, new ImageMetadata(imagePath));
    }

    @Override
    public void saveMetadataForImage(String imagePath, ImageMetadata metadata) throws IOException {
        Map<String, ImageMetadata> all = loadMetadata();
        all.put(imagePath, metadata);
        saveMetadata(all);
    }
}

