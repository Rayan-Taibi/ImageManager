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
 * Decryption filter that reverses pixel shuffling based on a password.
 * Uses the same SHA-256 seeding as EncryptionFilter for deterministic unshuffling.
 */
public class DecryptionFilter implements Filter {
    private final String password;

    public DecryptionFilter(String password) {
        this.password = password;
    }

    @Override
    public Image apply(Image source) {
        int width = (int) source.getWidth();
        int height = (int) source.getHeight();
        int totalPixels = width * height;

        // Create pixel index list (in same order as EncryptionFilter)
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < totalPixels; i++) {
            indices.add(i);
        }

        // Shuffle indices using password-seeded random (same as encryption)
        SecureRandom random = getRandomFromPassword();
        Collections.shuffle(indices, random);

        // Read encrypted pixels
        PixelReader reader = source.getPixelReader();
        Color[] encryptedPixels = new Color[totalPixels];
        for (int i = 0; i < totalPixels; i++) {
            int x = i % width;
            int y = i / width;
            encryptedPixels[i] = reader.getColor(x, y);
        }

        // Reverse the shuffling: map encrypted position back to original
        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();
        for (int newIndex = 0; newIndex < totalPixels; newIndex++) {
            int originalIndex = indices.get(newIndex);
            int originalX = originalIndex % width;
            int originalY = originalIndex / width;
            writer.setColor(originalX, originalY, encryptedPixels[newIndex]);
        }

        return result;
    }

    @Override
    public String getName() {
        return "Decryption";
    }

    /**
     * Create a SecureRandom seeded with the password hash (same as EncryptionFilter).
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