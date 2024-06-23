package com.example.Bookstore;

public interface IEntityMapper {
    Book map(long id, BookRq rq);
}
