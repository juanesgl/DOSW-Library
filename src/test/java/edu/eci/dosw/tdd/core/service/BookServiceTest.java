package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import edu.eci.dosw.tdd.persistence.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.mapper.BookEntityMapper;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;
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

    @Mock
    private BookEntityMapper bookEntityMapper;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;
    private BookEntity sampleEntity;

    @BeforeEach
    void setUp() {
        sampleBook = Book.builder()
                .title("Test Book")
                .author("Test Author")
                .totalQuantity(5)
                .availableQuantity(5)
                .build();
        sampleEntity = BookEntity.builder()
                .id(1L)
                .title("Test Book")
                .author("Test Author")
                .totalQuantity(5)
                .availableQuantity(5)
                .build();
    }

    @Test
    void addBook_ShouldAddBookSuccessfully() {
        doNothing().when(bookValidator).validate(any(Book.class));
        when(bookEntityMapper.toEntity(any(Book.class))).thenReturn(sampleEntity);
        when(bookRepository.save(any(BookEntity.class))).thenReturn(sampleEntity);
        when(bookEntityMapper.toModel(any(BookEntity.class))).thenReturn(
                Book.builder().id(1L).title("Test Book").author("Test Author").totalQuantity(5).availableQuantity(5)
                        .build());

        Book addedBook = bookService.addBook(sampleBook);

        assertNotNull(addedBook.getId());
        assertEquals("Test Book", addedBook.getTitle());
        assertEquals(5, addedBook.getTotalQuantity());
        verify(bookValidator, times(1)).validate(sampleBook);
        verify(bookRepository, times(1)).save(any(BookEntity.class));
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
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(sampleEntity));
        when(bookEntityMapper.toModel(any(BookEntity.class))).thenReturn(sampleBook);

        List<Book> inventory = bookService.getAllBooks();

        assertEquals(1, inventory.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_ShouldReturnBook_WhenExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
        when(bookEntityMapper.toModel(sampleEntity)).thenReturn(sampleBook);

        Optional<Book> foundBook = bookService.getBookById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals("Test Book", foundBook.get().getTitle());
    }

    @Test
    void isBookAvailable_ShouldReturnTrue_WhenQuantityIsGreaterThanZero() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));

        assertTrue(bookService.isBookAvailable(1L));
    }

    @Test
    void isBookAvailable_ShouldReturnFalse_WhenQuantityIsZero() {
        BookEntity zeroEntity = BookEntity.builder().id(2L).totalQuantity(5).availableQuantity(0).build();
        when(bookRepository.findById(2L)).thenReturn(Optional.of(zeroEntity));

        assertFalse(bookService.isBookAvailable(2L));
    }

    @Test
    void decreaseAvailableQuantity_ShouldDecrease() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
        when(bookRepository.save(any(BookEntity.class))).thenReturn(sampleEntity);

        bookService.decreaseAvailableQuantity(1L);

        assertEquals(4, sampleEntity.getAvailableQuantity());
        verify(bookRepository, times(1)).save(sampleEntity);
    }

    @Test
    void increaseAvailableQuantity_ShouldIncrease() {
        BookEntity entity = BookEntity.builder().id(1L).totalQuantity(5).availableQuantity(3).build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(bookRepository.save(any(BookEntity.class))).thenReturn(entity);

        bookService.increaseAvailableQuantity(1L);

        assertEquals(4, entity.getAvailableQuantity());
        verify(bookRepository, times(1)).save(entity);
    }

    @Test
    void increaseAvailableQuantity_ShouldThrow_WhenAlreadyAtMax() {
        BookEntity entity = BookEntity.builder().id(1L).totalQuantity(5).availableQuantity(5).build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> bookService.increaseAvailableQuantity(1L));
    }
}
