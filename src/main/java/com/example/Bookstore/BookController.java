package com.example.Bookstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 * TODO
 * OpenAPI (Swagger)
 * @see <a href="https://github.com/springdoc/springdoc-openapi">Springdoc</a>
 * 1) добавить зависимость на эту библиотеку в POM
 * 2) проставить аннотации на каждую ручку и описать, как ей пользоваться в сваггере
 * 3) попробовать запустить локально приложение и открыть http://localhost:8080/swagger-ui/index.html
 */
@CrossOrigin(origins="http://localhost:8081")
@RestController
@RequestMapping("/api")
public class BookController {
    @Autowired //Можно использовать Autowired, но в Spring по дефолту использует коструктор по умолчанию
    private BookRepository bookRepository;

    @Autowired
    private IEntityMapper entityMapper;

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(
            @RequestParam(required = false) String title
    ) {
        /*
            TODO избавиться от try catch в слое контроллеров на ExceptionHandler
            https://www.baeldung.com/exception-handling-for-rest-with-spring
         */
        try {
            List<Book> books = new ArrayList<Book>();

            if (title == null)
                books.addAll(bookRepository.findAll());
            else
                books.addAll(bookRepository.findByTitle(title)); //TODO зачем?

//            TODO почитать про Optional в Java
//            Optional.ofNullable(title)
//                    .ifPresentOrElse(
//                            t -> books.addAll(bookRepository.findByTitle(title)),
//                            () -> books.addAll(bookRepository.findAll())
//                    );

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
            @RequestBody BookRq book
    ) {
        // String -> Book
        // ObjectMapper (Jackson)
        // DTO - Data Transfer Object
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
            @RequestBody BookRq requestBody
    ) {
        var entity = bookRepository.findById(id);

        if (entity == null) {
            return new ResponseEntity<>("Cannot find Book with specified id (" + id + ")", HttpStatus.NOT_FOUND);
        }

        var mapped = entityMapper.map(id, requestBody);

        bookRepository.update(mapped);
        return new ResponseEntity<>("Book was updated successfully.", HttpStatus.OK);
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
