package com.imagemanager.model.filter;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;


public abstract class AbstractFilter implements Filter {
    @Override
    public Image apply(Image source){
        int width = (int)source.getWidth();
        int height = (int)source.getHeight();

        //WritableImage  --> cette classe nous permet de créer une nouvelle image modifiable (cest la résultat de la modification)
        //PixelReader --> pour lire les pixels de l'image source
        //PixelWriter --> pour écrire les pixels dans la nouvelle image

        WritableImage resultat = new WritableImage(width , height);
        PixelReader reader = source.getPixelReader();
        PixelWriter Writer = resultat.getPixelWriter();

        for(int i = 0 ; i < height ; i++ ){
            for(int j = 0 ; j < width ; j++){
                Color coleurOriginale = reader.getColor(j, i);
                Color coleurTransformee = transformColor(coleurOriginale);
                Writer.setColor(j, i, coleurTransformee);
            }
        }
        return resultat;

    }

    protected abstract Color transformColor(Color color);

}
