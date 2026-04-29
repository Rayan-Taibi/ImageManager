package com.imagemanager.model.filter;
import javafx.scene.paint.Color;

public class RGBSwapFilter extends AbstractFilter {

    @Override
    protected Color transformColor(Color couleur) {
        return new Color(
            couleur.getGreen(),
            couleur.getBlue(),
            couleur.getRed(),
            couleur.getOpacity()
        );
    }

    @Override
    public String getName() {
        return "RGB --> GBR";
    }
}
