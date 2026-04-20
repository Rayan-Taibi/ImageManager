package com.imagemanager.model.filter;

import javafx.scene.paint.Color;

public class EncryptionFilter extends AbstractFilter {
    
    private static final int ENCRYPTION_KEY = 0xABCD;
    
    @Override
    protected Color transformColor(Color color) {
        // Encrypt by XOR-ing each color channel with a key
        int r = (int) (color.getRed() * 255) ^ (ENCRYPTION_KEY & 0xFF);
        int g = (int) (color.getGreen() * 255) ^ ((ENCRYPTION_KEY >> 8) & 0xFF);
        int b = (int) (color.getBlue() * 255) ^ ((ENCRYPTION_KEY >> 16) & 0xFF);
        
        // Clamp values to 0-255 range
        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));
        
        return new Color(r / 255.0, g / 255.0, b / 255.0, color.getOpacity());
    }
    
    @Override
    public String getName() {
        return "Encryption";
    }
}
