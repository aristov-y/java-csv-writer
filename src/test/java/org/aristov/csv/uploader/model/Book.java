package org.aristov.csv.uploader.model;

import org.aristov.csv.uploader.annotations.*;
import org.aristov.csv.uploader.mappers.DateTimeMapper;
import org.aristov.csv.uploader.mappers.FieldMapper;

import java.time.LocalDate;
import java.util.Optional;

@CustomFields({
        @CustomField(
                field = "author.name",
                header = "Имя автора",
                serializer = @CsvSerializer(Book.AuthorMapper.class)
        )
})
public class Book {
    @CsvField("Name")
    private String name;
    @CsvField("Publish Date")
    @CsvSerializer(DateTimeMapper.class)
    private LocalDate publishDate;
    @NestedCsvField
    private Publisher publisher;
    @NestedCsvField
    private Author author;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public static class AuthorMapper implements FieldMapper<Book> {

        @Override
        public Object serialize(Book value) {
            return Optional.ofNullable(value).map(Book::getAuthor).map(Author::getName);
        }
    }
}
