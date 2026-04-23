package com.imagemanager.model.filter;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encryption filter that shuffles pixels based on a password.
 * Uses SecureRandom seeded with SHA-256 hash of the password.
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
        SecureRandom random = getRandomFromPassword();
        Collections.shuffle(indices, random);

        // Read source pixels
        PixelReader reader = source.getPixelReader();
        Color[] originalPixels = new Color[totalPixels];
        for (int i = 0; i < totalPixels; i++) {
            int x = i % width;
            int y = i / width;
            originalPixels[i] = reader.getColor(x, y);
        }

        // Write shuffled pixels to new image
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
     * Create a SecureRandom seeded with the password hash.
     *
     * @return SecureRandom instance
     */
    private SecureRandom getRandomFromPassword() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            SecureRandom random = new SecureRandom();
            random.setSeed(hash);
            return random;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}
