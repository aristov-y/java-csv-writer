package org.aristov.csv.uploader.mappers;

public class EmptyMapper implements FieldMapper<Object> {
    @Override
    public Object map(Object value) {
        return null;
    }
}
