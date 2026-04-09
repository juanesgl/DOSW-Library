package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.repository.BookRepository;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookValidator bookValidator;
    private final BookRepository bookRepository;

    @Transactional
    public Book addBook(Book book) {
        bookValidator.validate(book);

        if (book.getTotalQuantity() == null || book.getTotalQuantity() <= 0) {
            throw new IllegalArgumentException("La cantidad total de ejemplares debe ser mayor a 0.");
        }
        if (book.getAvailableQuantity() == null || book.getAvailableQuantity() < 0) {
            throw new IllegalArgumentException("La cantidad disponible no puede ser menor a 0.");
        }
        if (book.getAvailableQuantity() > book.getTotalQuantity()) {
            throw new IllegalArgumentException("La cantidad disponible no puede superar la cantidad total.");
        }

        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    public boolean isBookAvailable(String bookId) {
        return bookRepository.findById(bookId)
                .map(b -> b.getAvailableQuantity() > 0)
                .orElse(false);
    }

    @Transactional
    public void decreaseAvailableQuantity(String bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado con ID: " + bookId));
        if (book.getAvailableQuantity() <= 0) {
            throw new IllegalArgumentException("No hay ejemplares disponibles para prestar.");
        }
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);
    }

    @Transactional
    public void increaseAvailableQuantity(String bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado con ID: " + bookId));
        if (book.getAvailableQuantity() >= book.getTotalQuantity()) {
            throw new IllegalArgumentException("La cantidad disponible no puede superar el stock total.");
        }
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepository.save(book);
    }
}