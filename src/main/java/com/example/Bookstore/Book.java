package com.example.Bookstore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * TODO гуд, но чтобы не было столько boilerplate кода используем Lombok
 * Почитай про него и добавь в этот проект, чтобы не генерить столько говна из геттеров и сеттеров и тд
 */
@Getter
@Setter
@ToString
@Builder
@Schema(description = "Сущность книги")
public class Book {
    @Schema(description = "Номер книги")
    private long id;

    @Schema(description = "Название книги")
    private String title;

    @Schema(description = "Автор книги")
    private String author;

//    public Book(String title, String author) {
//        this.title = title;
//        this.author = author;
//    }
}
