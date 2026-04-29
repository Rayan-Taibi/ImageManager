package com.imagemanager.model.filter;
import javafx.scene.paint.Color;
public class NoireBlanc extends AbstractFilter {

    @Override
    protected Color transformColor(Color couleur) {
        double gris = (couleur.getRed() + couleur.getGreen() + couleur.getBlue()) / 3.0;
        return new Color(gris, gris, gris, couleur.getOpacity());
    }


    @Override
    public String getName() {
        return "Noir et Blanc";
    }
}
