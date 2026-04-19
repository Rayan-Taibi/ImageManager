package com.imagemanager.model.filter;

public interface Filter {
    Image apply( Image image);// Applique le filtre à une image et retourne la nouvelle image filtrée
}
