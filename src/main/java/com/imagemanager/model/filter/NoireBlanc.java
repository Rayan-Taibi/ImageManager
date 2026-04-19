package com.imagemanager.model.filter;
import javafx.scene.paint.Color;
public class NoireBlanc extends AbstractFilter {

    @Override
    protected Color transformColor(Color color) {
        // Calculate the luminance (gray level)
        // Standard formula: 0.2126R + 0.7152G + 0.0722B
        // Or simpler average: (R + G + B) / 3
        double gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3.0;

        // Return a new color where R, G, and B are all the same 'gray' value
        // The opacity (alpha) remains the same as the original
        return new Color(gray, gray, gray, color.getOpacity());
    }


    @Override
    public String getName() {
        return "Noir et Blanc";
    }
}
