package com.imagemanager.model.filter;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class PrewittFilter extends AbstractFilter {
    
    @Override
    public Image apply(Image imageSource) {
        int largeur = (int) imageSource.getWidth();
        int hauteur = (int) imageSource.getHeight();
        
        WritableImage imageResultante = new WritableImage(largeur, hauteur);
        PixelReader lecteurPixels = imageSource.getPixelReader();
        PixelWriter ecrivainPixels = imageResultante.getPixelWriter();
        
        // Noyaux Prewitt pour les contours.
        // Axe X = bords verticaux.
        int[][] noyauX = {
            {-1, 0, 1},
            {-1, 0, 1},
            {-1, 0, 1}
        };
        
        // Axe Y = bords horizontaux.
        int[][] noyauY = {
            {-1, -1, -1},
            {0, 0, 0},
            {1, 1, 1}
        };
        
        for (int ligne = 0; ligne < hauteur; ligne++) {
            for (int colonne = 0; colonne < largeur; colonne++) {
                double bordX = 0;
                double bordY = 0;
                
                // Compare le voisinage 3x3.
                for (int decalageY = -1; decalageY <= 1; decalageY++) {
                    for (int decalageX = -1; decalageX <= 1; decalageX++) {
                        int pixelX = Math.min(largeur - 1, Math.max(0, colonne + decalageX));
                        int pixelY = Math.min(hauteur - 1, Math.max(0, ligne + decalageY));
                        
                        Color couleur = lecteurPixels.getColor(pixelX, pixelY);
                        // Passage en gris.
                        double gris = (couleur.getRed() + couleur.getGreen() + couleur.getBlue()) / 3.0;
                        
                        bordX += gris * noyauX[decalageY + 1][decalageX + 1];
                        bordY += gris * noyauY[decalageY + 1][decalageX + 1];
                    }
                }
                
                // Intensite du contour.
                double intensite = Math.sqrt(bordX * bordX + bordY * bordY);
                intensite = Math.min(1.0, intensite);
                
                ecrivainPixels.setColor(colonne, ligne, new Color(intensite, intensite, intensite, lecteurPixels.getColor(colonne, ligne).getOpacity()));
            }
        }
        
        return imageResultante;
    }
    
    @Override
    protected Color transformColor(Color couleur) {
        // Inutile ici, apply() est redefini.
        return couleur;
    }
    
    @Override
    public String getName() {
        return "Prewitt Edge Detection";
    }
}
