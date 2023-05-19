package org.aristov.csv.uploader.mappers;

public class SelfMapper implements FieldMapper<Object> {
    @Override
    public Object serialize(Object value) {
        return value;
    }
}
