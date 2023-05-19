package org.aristov.csv.uploader.mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeMapper implements FieldMapper<LocalDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    @Override
    public Object serialize(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return FORMATTER.format(value);
    }
}
