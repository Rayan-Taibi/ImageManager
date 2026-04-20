package com.imagemanager.model.filter;

import javafx.scene.image.Image;

public interface Filter {
    Image apply( Image image);  // Applique le filtre à une image et retourne la nouvelle image filtrée
    String getName(); 
}

