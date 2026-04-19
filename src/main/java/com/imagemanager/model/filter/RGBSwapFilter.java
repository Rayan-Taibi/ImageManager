package com.imagemanager.model.filter;

import javafx.scene.paint.Color;

public class RGBSwapFilter extends AbstractFilter {

    protected Color transformColor(Color color){
        return new Color(
            color.getGreen(),
            color.getBlue(),
            color.getRed(),
        );
    }
}
