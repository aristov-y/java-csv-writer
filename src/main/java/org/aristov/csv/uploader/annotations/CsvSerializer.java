package org.aristov.csv.uploader.annotations;

import org.aristov.csv.uploader.mappers.FieldMapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvSerializer {
    Class<? extends FieldMapper<?>> value();
}
