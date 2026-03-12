package edu.eci.dosw.tdd.service;
import edu.eci.dosw.tdd.exception.BookNotAvaibleException;
import edu.eci.dosw.tdd.model.Book;
import edu.eci.dosw.tdd.model.Loan;
import edu.eci.dosw.tdd.model.LoanStatus;
import edu.eci.dosw.tdd.model.User;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {
    
    private List<Loan> loans = new ArrayList<>();
    private final UserService userService;
    private final BookService bookService;

    public LoanService(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    public Loan createLoan(String userId, String bookId) throws BookNotAvaibleException {
        User user = userService.getUserByID(userId)
                .orElseThrow(() -> new IllegalArgumentException(BookNotAvaibleException.USER_NOT_FOUND + userId));

        Book book = bookService.getBookByID(bookId)
                .orElseThrow(() -> new IllegalArgumentException(BookNotAvaibleException.BOOK_NOT_FOUND + bookId));

        if (!bookService.isBookAvailable(book)) {
            throw new BookNotAvaibleException(BookNotAvaibleException.BOOK_NOT_AVAILABLE);
        }

        Loan newLoan = new Loan(book, user, LocalDate.now(), LoanStatus.ACTIVE, null);
        loans.add(newLoan);

        bookService.updateBookAvailability(book, -1);

        return newLoan;
    }

    public Loan returnLoan(String userId, String bookId) {
        Loan activeLoan = loans.stream()
                .filter(loan -> loan.getUser().getID().equals(userId)
                        && loan.getBook().getID().equals(bookId)
                        && loan.getStatus() == LoanStatus.ACTIVE)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("NO ACTIVE LOAN FOUND FOR USER: " + userId + " AND BOOK: " + bookId + " "));

        activeLoan.setStatus(LoanStatus.RETURNED);
        activeLoan.setReturnDate(LocalDate.now());

        bookService.updateBookAvailability(activeLoan.getBook(), 1);

        return activeLoan;
    }

    public List<Loan> getAllLoans() {
        return loans;
    }

}
