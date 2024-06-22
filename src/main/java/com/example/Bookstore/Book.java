package com.example.Bookstore;

/**
 * TODO гуд, но чтобы не было столько boilerplate кода используем Lombok
 * Почитай про него и добавь в этот проект, чтобы не генерить столько говна из геттеров и сеттеров и тд
 */
public class Book {
    private long id;
    private String title;
    private String author;

    public Book() {

    }

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book [id = " + id + ", title = " + title + ", author = " + author + "]";
    }
}
