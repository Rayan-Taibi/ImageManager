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
 * Decryption filter that reverses pixel shuffling based on a password.
 * Uses the same deterministic random as EncryptionFilter to reverse the shuffle.
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

        // Create pixel index list
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < totalPixels; i++) {
            indices.add(i);
        }

        // Get the SAME shuffle order as encryption (seed with password hash)
        Random random = getRandomFromPassword();
        Collections.shuffle(indices, random);

        // Read encrypted pixels
        PixelReader reader = source.getPixelReader();
        Color[] encryptedPixels = new Color[totalPixels];
        for (int i = 0; i < totalPixels; i++) {
            int x = i % width;
            int y = i / width;
            encryptedPixels[i] = reader.getColor(x, y);
        }

        // Create result image
        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();

        // To decrypt: reverse the shuffle
        // If encryption put pixel from originalIndex into shuffledIndex position,
        // Then decryption takes pixel from shuffledIndex and puts it back at originalIndex
        for (int shuffledPos = 0; shuffledPos < totalPixels; shuffledPos++) {
            int originalIndex = indices.get(shuffledPos);
            int originalX = originalIndex % width;
            int originalY = originalIndex / width;
            writer.setColor(originalX, originalY, encryptedPixels[shuffledPos]);
        }

        return result;
    }

    @Override
    public String getName() {
        return "Decryption";
    }

    /**
     * Create a Random seeded with the password hash (same as EncryptionFilter).
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
