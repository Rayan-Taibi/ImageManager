package com.imagemanager.model.metadata;

/**
 * Record representing a transformation applied to an image.
 * Immutable data holder for filter/transform operations.
 */
public record Transformation(
    String name,
    String type,
    String parameter
) {
    public Transformation(String name, String type) {
        this(name, type, null);
    }
}
