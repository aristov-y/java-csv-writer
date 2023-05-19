package org.aristov.csv.uploader.mappers;

public class SelfMapper implements FieldMapper<Object> {
    @Override
    public Object map(Object value) {
        return value;
    }
}
