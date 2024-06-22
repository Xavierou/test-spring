package com.example.Bookstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcBookRepository implements BookRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public int save(Book book) {
        return jdbcTemplate.update("INSERT INTO book (title, author) VALUES (?, ?)",
                new Object[] { book.getTitle(), book.getAuthor() }); // не уверен, что так хорошо, но выебываться не буду
    }

    @Override
    public int update(Book book) {
        return jdbcTemplate.update("UPDATE book SET title=?, author=? WHERE id=?",
                new Object[] { book.getTitle(), book.getAuthor(), book.getId() });
    }

    @Override
    public Book findById(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM book WHERE id=?",
                    BeanPropertyRowMapper.newInstance(Book.class), id);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null; // TOOD лучше возвращать Optional в таком случае
        }
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * FROM book", BeanPropertyRowMapper.newInstance(Book.class));
    }

    @Override
    public List<Book> findByTitle(String title) {
        return jdbcTemplate.query("SELECT * FROM book WHERE title=?",
                BeanPropertyRowMapper.newInstance(Book.class), title);
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("DELETE FROM book");
    }
}
