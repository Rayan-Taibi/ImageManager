package com.imagemanager.model.filter;

import javafx.scene.image.Image;

public interface Filter {
    Image apply(Image imageSource);
    String getName();
}

