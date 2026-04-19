package com.imagemanager.model.filter;
import javafx.scene.paint.Color;


public class SepiaFilter extends AbstractFilter {

    @Override
    protected Color transformColor(Color couleur){

        double inputRed = couleur.getRed();
        double inputGreen = couleur.getGreen();
        double inputBlue = couleur.getBlue();
        double outputRed = (inputRed * .393) + (inputGreen *.769) + (inputBlue * .189);
        double outputGreen = (inputRed * .349) + (inputGreen *.686) + (inputBlue * .168);
        double outputBlue = (inputRed * .272) + (inputGreen *.534) + (inputBlue * .131);

        outputRed = Math.min(1.0, outputRed);
        outputGreen = Math.min(1.0, outputGreen);
        outputBlue = Math.min(1.0, outputBlue);
        return new Color (outputRed , outputGreen , outputBlue , couleur.getOpacity());

    }
    @Override
    public String getName() {
        return "Sépia";
    }

}
