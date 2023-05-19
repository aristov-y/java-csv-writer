package org.aristov.csv.uploader.mappers;

@FunctionalInterface
public interface FieldMapper<T> {
    Object map(T value);
}
