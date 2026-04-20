package com.imagemanager.model.transformation;

public interface Transformation {
    void apply(Image image);
    String getName();
}
