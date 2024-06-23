package com.example.Bookstore;

import lombok.*;

/**
 * TODO гуд, но чтобы не было столько boilerplate кода используем Lombok
 * Почитай про него и добавь в этот проект, чтобы не генерить столько говна из геттеров и сеттеров и тд
 */
@Getter
@Setter
@ToString
@Builder
public class Book {
    private long id;
    private String title;
    private String author;
    private String subauthors;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }
}
