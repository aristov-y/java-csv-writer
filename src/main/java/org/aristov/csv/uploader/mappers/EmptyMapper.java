package org.aristov.csv.uploader.mappers;

public class EmptyMapper implements FieldMapper<Object> {
    @Override
    public Object serialize(Object value) {
        return null;
    }
}
