package com.imagemanager.model.filter;
import javafx.scene.paint.Color;
public class NoireBlanc extends AbstractFilter {

    @Override
    protected Color transformColor(Color color) {
        double gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3.0;
        return new Color(gray, gray, gray, color.getOpacity());
    }


    @Override
    public String getName() {
        return "Noir et Blanc";
    }
}
