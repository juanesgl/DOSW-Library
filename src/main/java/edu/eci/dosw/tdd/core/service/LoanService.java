package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvaibleException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.LoanStatus;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.validator.LoanValidator;
import edu.eci.dosw.tdd.core.util.DateUtil;
import edu.eci.dosw.tdd.persistence.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.entity.LoanEntity;
import edu.eci.dosw.tdd.persistence.entity.UserEntity;
import edu.eci.dosw.tdd.persistence.mapper.LoanEntityMapper;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;
import edu.eci.dosw.tdd.persistence.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final LoanEntityMapper loanEntityMapper;
    private final LoanValidator loanValidator;
    private final BookService bookService;

    @Transactional
    public Loan createLoan(Long userId, Long bookId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("El usuario con ID " + userId + " no existe."));

        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("El libro con ID " + bookId + " no existe."));

        User user = User.builder().id(userEntity.getId()).name(userEntity.getName()).build();
        Book book = Book.builder().id(bookEntity.getId()).title(bookEntity.getTitle()).author(bookEntity.getAuthor())
                .build();
        loanValidator.validateLoanCreation(user, book);

        if (!bookService.isBookAvailable(bookId)) {
            throw new BookNotAvaibleException("El libro '" + bookEntity.getTitle() + "' no está disponible.");
        }

        long activeLoans = loanRepository.countByUserIdAndStatus(userId, LoanStatus.ACTIVE);
        if (activeLoans >= 3) {
            throw new LoanLimitExceededException("El usuario ya alcanzó el límite máximo de libros prestados.");
        }

        LoanEntity newLoanEntity = LoanEntity.builder()
                .book(bookEntity)
                .user(userEntity)
                .loanDate(DateUtil.getCurrentDate())
                .status(LoanStatus.ACTIVE)
                .build();

        LoanEntity saved = loanRepository.save(newLoanEntity);
        bookService.decreaseAvailableQuantity(bookId);

        return loanEntityMapper.toModel(saved);
    }

    @Transactional
    public Loan returnLoan(Long userId, Long bookId) {
        LoanEntity activeLoan = loanRepository.findFirstByUserIdAndBookIdAndStatus(userId, bookId, LoanStatus.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró un préstamo activo para este usuario y libro."));

        activeLoan.setStatus(LoanStatus.RETURNED);
        activeLoan.setReturnDate(DateUtil.getCurrentDate());

        LoanEntity saved = loanRepository.save(activeLoan);
        bookService.increaseAvailableQuantity(bookId);

        return loanEntityMapper.toModel(saved);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(loanEntityMapper::toModel)
                .collect(Collectors.toList());
    }
}