package org.aristov.csv.uploader.generator;

import org.aristov.csv.uploader.annotations.*;
import org.aristov.csv.uploader.mappers.FieldMapper;
import org.aristov.csv.uploader.mappers.SelfMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CsvGenerator<T> {
    private final Class<T> mainClass;
    private final Set<String> customFields;
    private final Map<String, String> headerNames;
    private final Map<String, FieldMapper<?>> cachedMappers;

    public CsvGenerator(Class<T> clazz) {
        this.mainClass = clazz;
        this.customFields = new HashSet<>();
        this.headerNames = new ConcurrentHashMap<>();
        this.cachedMappers = new ConcurrentHashMap<>();
        this.generateMappers(this.mainClass);
    }

    private void generateMappers(Class<?> clazz) {
        if (clazz == null || Object.class.equals(clazz)) {
            return;
        }
        Class<?> superclass = clazz.getSuperclass();
        this.generateMappers(superclass);
        this.generateFieldMappers(null, clazz);
    }

    private void generateFieldMappers(String prefix, Class<?> clazz) {
        this.generateCustomMappers(prefix, clazz);
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            String fieldName;
            if (prefix == null) {
                fieldName = declaredField.getName();
            } else {
                fieldName = String.format("%s.%s", prefix, declaredField.getName());
            }
            if (declaredField.isAnnotationPresent(NestedCsvField.class)) {
                Class<?> type = declaredField.getType();
                this.generateFieldMappers(fieldName, type);
            } else {
                if (this.customFields.contains(fieldName)) {
                    continue;
                }
                CsvField csvField = declaredField.getAnnotation(CsvField.class);
                if (csvField != null) {
                    CsvSerializer csvSerializer = declaredField.getAnnotation(CsvSerializer.class);
                    this.headerNames.put(fieldName, csvField.value());
                    if (csvSerializer == null) {
                        this.cachedMappers.put(fieldName, new SelfMapper());
                    } else {
                        this.cachedMappers.put(fieldName, createMapperInstance(csvSerializer.value()));
                    }
                }
            }
        }
    }

    private void generateCustomMappers(String prefix, Class<?> clazz) {
        CustomFields fieldsAnnotation = clazz.getAnnotation(CustomFields.class);
        if (fieldsAnnotation != null) {
            CustomField[] fields = fieldsAnnotation.value();
            for (CustomField field : fields) {
                String fieldName;
                if (prefix == null) {
                    fieldName = field.field();
                } else {
                    fieldName = String.format("%s.%s", prefix, field.field());
                }
                if (customFields.contains(fieldName)) {
                    continue;
                }
                Class<? extends FieldMapper<?>> mapperClass = field.serializer().value();
                this.cachedMappers.put(fieldName, this.createMapperInstance(mapperClass));
                this.headerNames.put(fieldName, field.header());
                this.customFields.add(fieldName);
            }
        }
    }

    private FieldMapper<?> createMapperInstance(Class<? extends FieldMapper<?>> mapperClass) {
        try {
            Constructor<? extends FieldMapper<?>> constructor = mapperClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
