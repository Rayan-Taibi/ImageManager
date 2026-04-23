package com.imagemanager.model.filter;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Encryption filter that shuffles pixels based on a password.
 * Uses Random seeded with SHA-256 hash of the password for deterministic shuffling.
 */
public class EncryptionFilter implements Filter {
    private final String password;

    public EncryptionFilter(String password) {
        this.password = password;
    }

    @Override
    public Image apply(Image source) {
        int width = (int) source.getWidth();
        int height = (int) source.getHeight();
        int totalPixels = width * height;

        // Create pixel index list
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < totalPixels; i++) {
            indices.add(i);
        }

        // Shuffle indices using password-seeded random
        Random random = getRandomFromPassword();
        Collections.shuffle(indices, random);

        // Read source pixels in linear order
        PixelReader reader = source.getPixelReader();
        Color[] originalPixels = new Color[totalPixels];
        for (int i = 0; i < totalPixels; i++) {
            int x = i % width;
            int y = i / width;
            originalPixels[i] = reader.getColor(x, y);
        }

        // Write shuffled pixels to new image
        // indices.get(i) tells us which original pixel goes to position i
        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();
        for (int newIndex = 0; newIndex < totalPixels; newIndex++) {
            int originalIndex = indices.get(newIndex);
            int newX = newIndex % width;
            int newY = newIndex / width;
            writer.setColor(newX, newY, originalPixels[originalIndex]);
        }

        return result;
    }

    @Override
    public String getName() {
        return "Encryption";
    }

    /**
     * Create a Random seeded with the password hash.
     * Uses long seed derived from SHA-256 hash for deterministic behavior.
     */
    private Random getRandomFromPassword() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            
            // Convert first 8 bytes to long seed
            long seed = 0;
            for (int i = 0; i < 8; i++) {
                seed = (seed << 8) | (hash[i] & 0xFF);
            }
            
            Random random = new Random(seed);
            return random;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}

