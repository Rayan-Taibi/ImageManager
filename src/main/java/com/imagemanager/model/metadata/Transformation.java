package com.imagemanager.model.metadata;

/**
 * Enregistrement d'une transformation appliquee a une image.
 * Donnée immuable pour les operations de filtre ou de transformation.
 */
public record Transformation(
    String nom,
    String typeTransformation,
    String parametre
) {
    public Transformation(String nom, String typeTransformation) {
        this(nom, typeTransformation, null);
    }
}
