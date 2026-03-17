package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "Inventory operations and book lookup")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @Operation(summary = "Add a book", description = "Adds a new book to the inventory with an initial quantity")
    public ResponseEntity<Book> addBook(@RequestBody Book book, @RequestParam(defaultValue = "1") int quantity) {
        Book createdBook = bookService.addBook(book, quantity);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get inventory", description = "Returns the book catalog along with its available quantity")
    public ResponseEntity<Map<Book, Integer>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{ID}")
    @Operation(summary = "Search book by ID", description = "Finds detailed information of a specific book")
    public ResponseEntity<Book> getBookByID(@PathVariable String ID) {
        Book book = bookService.getBookByID(ID)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + ID));
        return ResponseEntity.ok(book);
    }
}