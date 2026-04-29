package com.imagemanager.model.filter;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;


public abstract class AbstractFilter implements Filter {
    /**
     * Filtre générique basé sur une transformation pixel-par-pixel.
     *
     * Comportement : lit chaque pixel de l'image source, appelle
     * `transformColor` (implémenté par les sous-classes) puis écrit le pixel
     * transformé dans une nouvelle image. C'est simple et fiable mais coûteux
     * en CPU pour les grandes images — approprié pour des usages desktop.
     */
    @Override
    public Image apply(Image imageSource) {
        int largeur = (int) imageSource.getWidth();
        int hauteur = (int) imageSource.getHeight();

        // Img modifiable, puis lecture/ecriture pixel par pixel.

        WritableImage imageResultante = new WritableImage(largeur, hauteur);
        PixelReader lecteurPixels = imageSource.getPixelReader();
        PixelWriter ecrivainPixels = imageResultante.getPixelWriter();

        for (int ligne = 0; ligne < hauteur; ligne++) {
            for (int colonne = 0; colonne < largeur; colonne++) {
                Color couleurOriginale = lecteurPixels.getColor(colonne, ligne);
                Color couleurTransformee = transformColor(couleurOriginale);
                ecrivainPixels.setColor(colonne, ligne, couleurTransformee);
            }
        }
        return imageResultante;
    }

    protected abstract Color transformColor(Color couleur);

}
