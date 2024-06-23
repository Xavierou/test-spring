package com.example.Bookstore;

import org.springframework.stereotype.Component;

@Component
public class EntityMapper implements IEntityMapper {
    @Override
    public Book map(long id, BookRq rq) {
        return Book.builder()
                .id(id)
                .author(rq.getAuthor())
                .title(rq.getTitle())
                .build();
    }
}
