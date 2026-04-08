package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.repository.BookRepository;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookValidator bookValidator;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        sampleBook = Book.builder()
                .id("1")
                .title("Test Book")
                .author("Test Author")
                .totalQuantity(5)
                .availableQuantity(5)
                .build();
    }

    @Test
    void addBook_ShouldAddBookSuccessfully() {
        doNothing().when(bookValidator).validate(any(Book.class));
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);

        Book addedBook = bookService.addBook(sampleBook);

        assertNotNull(addedBook.getId());
        assertEquals("Test Book", addedBook.getTitle());
        assertEquals(5, addedBook.getTotalQuantity());
        verify(bookValidator, times(1)).validate(sampleBook);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void addBook_ShouldThrowException_WhenTotalQuantityIsZero() {
        doNothing().when(bookValidator).validate(any(Book.class));
        Book invalidBook = Book.builder().title("Book").author("Author").totalQuantity(0).availableQuantity(0).build();

        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(invalidBook));
    }

    @Test
    void addBook_ShouldThrowException_WhenAvailableQuantityIsNegative() {
        doNothing().when(bookValidator).validate(any(Book.class));
        Book invalidBook = Book.builder().title("Book").author("Author").totalQuantity(5).availableQuantity(-1).build();

        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(invalidBook));
    }

    @Test
    void addBook_ShouldThrowException_WhenAvailableExceedsTotal() {
        doNothing().when(bookValidator).validate(any(Book.class));
        Book invalidBook = Book.builder().title("Book").author("Author").totalQuantity(3).availableQuantity(5).build();

        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(invalidBook));
    }

    @Test
    void getAllBooks_ShouldReturnInventory() {
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(sampleBook));

        List<Book> inventory = bookService.getAllBooks();

        assertEquals(1, inventory.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_ShouldReturnBook_WhenExists() {
        when(bookRepository.findById("1")).thenReturn(Optional.of(sampleBook));

        Optional<Book> foundBook = bookService.getBookById("1");

        assertTrue(foundBook.isPresent());
        assertEquals("Test Book", foundBook.get().getTitle());
    }

    @Test
    void isBookAvailable_ShouldReturnTrue_WhenQuantityIsGreaterThanZero() {
        when(bookRepository.findById("1")).thenReturn(Optional.of(sampleBook));

        assertTrue(bookService.isBookAvailable("1"));
    }

    @Test
    void isBookAvailable_ShouldReturnFalse_WhenQuantityIsZero() {
        Book zeroBook = Book.builder().id("2").totalQuantity(5).availableQuantity(0).build();
        when(bookRepository.findById("2")).thenReturn(Optional.of(zeroBook));

        assertFalse(bookService.isBookAvailable("2"));
    }

    @Test
    void decreaseAvailableQuantity_ShouldDecrease() {
        when(bookRepository.findById("1")).thenReturn(Optional.of(sampleBook));
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);

        bookService.decreaseAvailableQuantity("1");

        assertEquals(4, sampleBook.getAvailableQuantity());
        verify(bookRepository, times(1)).save(sampleBook);
    }

    @Test
    void increaseAvailableQuantity_ShouldIncrease() {
        Book book = Book.builder().id("1").totalQuantity(5).availableQuantity(3).build();
        when(bookRepository.findById("1")).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        bookService.increaseAvailableQuantity("1");

        assertEquals(4, book.getAvailableQuantity());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void increaseAvailableQuantity_ShouldThrow_WhenAlreadyAtMax() {
        Book book = Book.builder().id("1").totalQuantity(5).availableQuantity(5).build();
        when(bookRepository.findById("1")).thenReturn(Optional.of(book));

        assertThrows(IllegalArgumentException.class, () -> bookService.increaseAvailableQuantity("1"));
    }

    @Test
    void addBook_ShouldThrowException_WhenTotalQuantityIsNull() {
        doNothing().when(bookValidator).validate(any(Book.class));
        Book invalidBook = Book.builder().title("Book").author("Author").totalQuantity(null).availableQuantity(3)
                .build();

        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(invalidBook));
    }

    @Test
    void addBook_ShouldThrowException_WhenAvailableQuantityIsNull() {
        doNothing().when(bookValidator).validate(any(Book.class));
        Book invalidBook = Book.builder().title("Book").author("Author").totalQuantity(5).availableQuantity(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(invalidBook));
    }

    @Test
    void isBookAvailable_ShouldReturnFalse_WhenBookNotFound() {
        when(bookRepository.findById("99")).thenReturn(Optional.empty());

        assertFalse(bookService.isBookAvailable("99"));
    }

    @Test
    void getBookById_ShouldReturnEmpty_WhenBookDoesNotExist() {
        when(bookRepository.findById("99")).thenReturn(Optional.empty());

        Optional<Book> result = bookService.getBookById("99");

        assertFalse(result.isPresent());
    }

    @Test
    void decreaseAvailableQuantity_ShouldThrow_WhenBookNotFound() {
        when(bookRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bookService.decreaseAvailableQuantity("99"));
    }

    @Test
    void decreaseAvailableQuantity_ShouldThrow_WhenNoAvailableCopies() {
        Book book = Book.builder().id("1").totalQuantity(5).availableQuantity(0).build();
        when(bookRepository.findById("1")).thenReturn(Optional.of(book));

        assertThrows(IllegalArgumentException.class, () -> bookService.decreaseAvailableQuantity("1"));
    }

    @Test
    void increaseAvailableQuantity_ShouldThrow_WhenBookNotFound() {
        when(bookRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bookService.increaseAvailableQuantity("99"));
    }
}
