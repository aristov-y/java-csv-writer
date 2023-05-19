package org.aristov.csv.uploader.mappers;

@FunctionalInterface
public interface FieldMapper<T> {
    Object serialize(T value);
}
