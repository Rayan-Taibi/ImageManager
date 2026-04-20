package com.imagemanager.model.filter;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class PrewittFilter extends AbstractFilter {
    
    @Override
    public Image apply(Image source) {
        int width = (int) source.getWidth();
        int height = (int) source.getHeight();
        
        WritableImage result = new WritableImage(width, height);
        PixelReader reader = source.getPixelReader();
        PixelWriter writer = result.getPixelWriter();
        
        // Prewitt kernels for edge detection
        // Horizontal kernel (detects vertical edges)
        int[][] kernelX = {
            {-1, 0, 1},
            {-1, 0, 1},
            {-1, 0, 1}
        };
        
        // Vertical kernel (detects horizontal edges)
        int[][] kernelY = {
            {-1, -1, -1},
            {0, 0, 0},
            {1, 1, 1}
        };
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double edgeX = 0;
                double edgeY = 0;
                
                // Apply kernels to the 3x3 neighborhood
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int px = Math.min(width - 1, Math.max(0, x + kx));
                        int py = Math.min(height - 1, Math.max(0, y + ky));
                        
                        Color color = reader.getColor(px, py);
                        // Convert to grayscale
                        double gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3.0;
                        
                        edgeX += gray * kernelX[ky + 1][kx + 1];
                        edgeY += gray * kernelY[ky + 1][kx + 1];
                    }
                }
                
                // Calculate edge magnitude
                double magnitude = Math.sqrt(edgeX * edgeX + edgeY * edgeY);
                magnitude = Math.min(1.0, magnitude);
                
                writer.setColor(x, y, new Color(magnitude, magnitude, magnitude, reader.getColor(x, y).getOpacity()));
            }
        }
        
        return result;
    }
    
    @Override
    protected Color transformColor(Color color) {
        // Not used since we override apply()
        return color;
    }
    
    @Override
    public String getName() {
        return "Prewitt Edge Detection";
    }
}
