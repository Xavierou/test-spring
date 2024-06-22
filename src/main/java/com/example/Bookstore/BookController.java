package com.example.Bookstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 */
@CrossOrigin(origins="http://localhost:8081")
@RestController
@RequestMapping("/api")
public class BookController {
    @Autowired //Можно использовать Autowired, но в Spring по дефолту использует коструктор по умолчанию
    BookRepository bookRepository;

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(
            @RequestParam(required = false) String title
    ) {
        try {
            List<Book> books = new ArrayList<Book>();

            if (title == null)
                books.addAll(bookRepository.findAll());
            else
                books.addAll(bookRepository.findByTitle(title)); //TODO зачем?

            if (books.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); //TODO можно по-другому
        }
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(
         @PathVariable("id") long id
    ) {
        Book book = bookRepository.findById(id);

        // TODO ФП вариант
        return Optional.ofNullable(book)
                .map(b -> ResponseEntity.ok().body(b))
                .orElse(ResponseEntity.notFound().build());

//        if (book != null)
//            return new ResponseEntity<>(book, HttpStatus.OK);
//        else
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/books") //TODO лучше /book, так как ты одну книгу добавляешь
    public ResponseEntity<String> addBook(
            @RequestBody Book book
    ) {
        try {
            bookRepository.save(new Book(book.getTitle(), book.getAuthor()));
            return new ResponseEntity<>("Book was added successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.internalServerError().build(); //TODO так лучше, без null ссылок
        }
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<String> updateBook(
            @PathVariable("id") long id,
            @RequestBody Book book
    ) {
        Book _book = bookRepository.findById(id);

        if (_book != null) {
            _book.setId(id); //TODO маппинги лучше декомпозировать в отдельный метод и класс маппер
            _book.setTitle(book.getTitle());
            _book.setAuthor(book.getAuthor());

            bookRepository.update(_book);
            return new ResponseEntity<>("Book was updated successfully.", HttpStatus.OK);
        } else
            return new ResponseEntity<>("Cannot find Book with specified id (" + id + ")", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/tutorials/{id}")
    public ResponseEntity<String> deleteBook(
            @PathVariable("id") long id
    ) {
        try {
            int result = bookRepository.deleteById(id);
            if (result == 0)
                return new ResponseEntity<>("Cannot find Book with specified id" + id, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>("Book was deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete book.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deleteAllBooks() {
        try {
            int numRows = bookRepository.deleteAll();
            return new ResponseEntity<>("Deleted " + numRows + " book(s).", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete books", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
