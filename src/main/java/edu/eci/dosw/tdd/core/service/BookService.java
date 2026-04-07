package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import edu.eci.dosw.tdd.persistence.relational.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.relational.mapper.BookEntityMapper;
import edu.eci.dosw.tdd.persistence.relational.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookValidator bookValidator;
    private final BookRepository bookRepository;
    private final BookEntityMapper bookEntityMapper;

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

        BookEntity entity = bookEntityMapper.toEntity(book);
        BookEntity saved = bookRepository.save(entity);
        return bookEntityMapper.toModel(saved);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookEntityMapper::toModel)
                .collect(Collectors.toList());
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookEntityMapper::toModel);
    }

    public boolean isBookAvailable(Long bookId) {
        return bookRepository.findById(bookId)
                .map(b -> b.getAvailableQuantity() > 0)
                .orElse(false);
    }

    @Transactional
    public void decreaseAvailableQuantity(Long bookId) {
        BookEntity entity = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado con ID: " + bookId));
        if (entity.getAvailableQuantity() <= 0) {
            throw new IllegalArgumentException("No hay ejemplares disponibles para prestar.");
        }
        entity.setAvailableQuantity(entity.getAvailableQuantity() - 1);
        bookRepository.save(entity);
    }

    @Transactional
    public void increaseAvailableQuantity(Long bookId) {
        BookEntity entity = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado con ID: " + bookId));
        if (entity.getAvailableQuantity() >= entity.getTotalQuantity()) {
            throw new IllegalArgumentException("La cantidad disponible no puede superar el stock total.");
        }
        entity.setAvailableQuantity(entity.getAvailableQuantity() + 1);
        bookRepository.save(entity);
    }
}