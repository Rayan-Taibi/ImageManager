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

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < totalPixels; i++) {
            indices.add(i);
        }

        // Must use the EXACT same SecureRandom setup as EncryptionFilter
        SecureRandom secureRandom = getSecureRandomFromPassword();
        Collections.shuffle(indices, secureRandom);

        PixelReader reader = source.getPixelReader();
        Color[] encryptedPixels = new Color[totalPixels];
        for (int i = 0; i < totalPixels; i++) {
            encryptedPixels[i] = reader.getColor(i % width, i / width);
        }

        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();

        for (int shuffledPos = 0; shuffledPos < totalPixels; shuffledPos++) {
            int originalIndex = indices.get(shuffledPos);
            writer.setColor(originalIndex % width, originalIndex / width, encryptedPixels[shuffledPos]);
        }

        return result;
    }

    @Override
    public String getName() {
        return "Decryption";
    }

    private SecureRandom getSecureRandomFromPassword() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] seed = digest.digest(password.getBytes());
            
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(seed);
            return sr;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm not available", e);
        }
    }
}