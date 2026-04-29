package com.imagemanager.model.filter;
import javafx.scene.paint.Color;


public class SepiaFilter extends AbstractFilter {

    @Override
    protected Color transformColor(Color couleur) {

        double rougeEntree = couleur.getRed();
        double vertEntree = couleur.getGreen();
        double bleuEntree = couleur.getBlue();
        double rougeSortie = (rougeEntree * .393) + (vertEntree * .769) + (bleuEntree * .189);
        double vertSortie = (rougeEntree * .349) + (vertEntree * .686) + (bleuEntree * .168);
        double bleuSortie = (rougeEntree * .272) + (vertEntree * .534) + (bleuEntree * .131);

        rougeSortie = Math.min(1.0, rougeSortie);
        vertSortie = Math.min(1.0, vertSortie);
        bleuSortie = Math.min(1.0, bleuSortie);
        return new Color(rougeSortie, vertSortie, bleuSortie, couleur.getOpacity());

    }
    @Override
    public String getName() {
        return "Sépia";
    }

}
