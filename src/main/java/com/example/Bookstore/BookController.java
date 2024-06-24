package com.example.Bookstore;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

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

@Tag(
        name = "Книжный контроллер",
        description = "Управление всеми операциями с книгами"
)

@RestController
@RequestMapping("/api")
public class BookController {
    @Autowired //Можно использовать Autowired, но в Spring по дефолту использует коструктор по умолчанию
    private BookRepository bookRepository;

    @Autowired
    private IEntityMapper entityMapper;

    @Operation(
            summary = "Список книг",
            description = "Позволяет получить полный список книг"
    )
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(
            @RequestParam(required = false)
            @Parameter(description = "Название книги; необязательно") String title
    ) {
        /*
            TODO избавиться от try catch в слое контроллеров на ExceptionHandler
            https://www.baeldung.com/exception-handling-for-rest-with-spring
         */
        try {
            List<Book> books = new ArrayList<>();

//            if (title == null)
//                books.addAll(bookRepository.findAll());
//            else
//                books.addAll(bookRepository.findByTitle(title)); //TODO зачем?

//          TODO почитать про Optional в Java
            Optional.ofNullable(title)
                    .ifPresentOrElse(
                            t -> books.addAll(bookRepository.findByTitle(title)),
                            () -> books.addAll(bookRepository.findAll())
                    );

            if (books.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch(Exception e) {
            return ResponseEntity.internalServerError().build(); //TODO можно по-другому
        }
    }

    @Operation(
            summary = "Выбор книг",
            description = "Позволяет получить конкретную книгу по ее номеру"
    )
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(
         @PathVariable("id")
         @Parameter(description = "Номер искомой книги", required = true) long id
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
    @Operation(
            summary = "Добавление книг",
            description = "Позволяет добавить книгу в общий список"
    )
    @PostMapping("/book")
    public ResponseEntity<String> addBook(
            @Parameter(description = "JSON с необходимой информацией о книге", required = true)
            @RequestBody BookRq reqBody
    ) {
        // String -> Book
        // ObjectMapper (Jackson)
        // DTO - Data Transfer Object
//        try {
//            bookRepository.save(entityMapper.map(0, reqBody));
//            return ResponseEntity.ok("Book added successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().build(); //TODO так лучше, без null ссылок
//        }
        bookRepository.save(entityMapper.map(0, reqBody));
        return ResponseEntity.ok("Book added successfully.");
    }

    @Operation(
            summary = "Обновление книг",
            description = "Позволяет обновить информацию по какой-то конкретной книге"
    )
    @PutMapping("/books/{id}")
    public ResponseEntity<String> updateBook(
            @PathVariable("id")
            @Parameter(description = "Номер интересующей книги", required = true) long id,
            @Parameter(description = "JSON с информацией на замену", required = true)
            @RequestBody BookRq requestBody
    ) {
        var entity = bookRepository.findById(id);

        if (entity == null) {
            return ResponseEntity.notFound().build();
        }

        var mapped = entityMapper.map(id, requestBody);

        bookRepository.update(mapped);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Удаление книг",
            description = "Позволяет удалить конкретную книгу из таблицы"
    )
    @DeleteMapping("/books/{id}")
    public ResponseEntity<String> deleteBook(
            @PathVariable("id")
            @Parameter(description = "Номер интересующей книги", required = true) long id
    ) {
//        try {
//            int result = bookRepository.deleteById(id);
//            if (result == 0)
//                return ResponseEntity.notFound().build();
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().build();
//        }
        int result = bookRepository.deleteById(id);
        if (result == 0)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Полное удаление",
            description = "Позволяет полностью удалить все содержимое БД... кто нажмет тот пидор"
    )
    @DeleteMapping("/books")
    public ResponseEntity<String> deleteAllBooks() {
//        try {
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().build();
//        }
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<ErrorMessage> handleException(HttpServerErrorException.InternalServerError exc) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exc.getMessage()));
    }
}
