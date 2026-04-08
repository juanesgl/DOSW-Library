package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvaibleException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.LoanStatus;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.repository.BookRepository;
import edu.eci.dosw.tdd.core.repository.LoanRepository;
import edu.eci.dosw.tdd.core.repository.UserRepository;
import edu.eci.dosw.tdd.core.validator.LoanValidator;
import edu.eci.dosw.tdd.core.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final LoanValidator loanValidator;
    private final BookService bookService;

    @Transactional
    public Loan createLoan(String userId, String bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("El usuario con ID " + userId + " no existe."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("El libro con ID " + bookId + " no existe."));

        loanValidator.validateLoanCreation(user, book);

        if (!bookService.isBookAvailable(bookId)) {
            throw new BookNotAvaibleException("El libro '" + book.getTitle() + "' no está disponible.");
        }

        long activeLoans = loanRepository.countByUserIdAndStatus(userId, LoanStatus.ACTIVE);
        if (activeLoans >= 3) {
            throw new LoanLimitExceededException("El usuario ya alcanzó el límite máximo de libros prestados.");
        }

        Loan newLoan = Loan.builder()
                .book(book)
                .user(user)
                .loanDate(DateUtil.getCurrentDate())
                .status(LoanStatus.ACTIVE)
                .build();

        Loan saved = loanRepository.save(newLoan);
        bookService.decreaseAvailableQuantity(bookId);

        return saved;
    }

    @Transactional
    public Loan returnLoan(String userId, String bookId) {
        Loan activeLoan = loanRepository.findFirstByUserIdAndBookIdAndStatus(userId, bookId, LoanStatus.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró un préstamo activo para este usuario y libro."));

        activeLoan.setStatus(LoanStatus.RETURNED);
        activeLoan.setReturnDate(DateUtil.getCurrentDate());

        Loan saved = loanRepository.save(activeLoan);
        bookService.increaseAvailableQuantity(bookId);

        return saved;
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public List<Loan> getLoansByUserId(String userId) {
        return loanRepository.findByUserId(userId);
    }
}