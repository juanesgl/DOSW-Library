package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvaibleException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.LoanStatus;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.validator.LoanValidator;
import edu.eci.dosw.tdd.core.repository.BookRepository;
import edu.eci.dosw.tdd.core.repository.LoanRepository;
import edu.eci.dosw.tdd.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanValidator loanValidator;

    @Mock
    private BookService bookService;

    @InjectMocks
    private LoanService loanService;

    private User sampleUser;
    private Book sampleBook;
    private Loan sampleLoan;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder().id("1").name("Test User").username("testuser").password("pass")
                .role("USER").build();
        sampleBook = Book.builder().id("1").title("Test Book").author("Author").totalQuantity(5)
                .availableQuantity(5).build();
        sampleLoan = Loan.builder().id("1")
                .user(sampleUser)
                .book(sampleBook)
                .loanDate(LocalDate.now()).status(LoanStatus.ACTIVE).build();
    }

    @Test
    void createLoan_ShouldCreateLoanSuccessfully() {
        when(userRepository.findById("1")).thenReturn(Optional.of(sampleUser));
        when(bookRepository.findById("1")).thenReturn(Optional.of(sampleBook));
        when(bookService.isBookAvailable("1")).thenReturn(true);
        when(loanRepository.countByUserIdAndStatus("1", LoanStatus.ACTIVE)).thenReturn(0L);
        when(loanRepository.save(any(Loan.class))).thenReturn(sampleLoan);
        doNothing().when(loanValidator).validateLoanCreation(any(), any());
        doNothing().when(bookService).decreaseAvailableQuantity("1");

        Loan loan = loanService.createLoan("1", "1");

        assertNotNull(loan);
        assertEquals(LoanStatus.ACTIVE, loan.getStatus());
        verify(bookService, times(1)).decreaseAvailableQuantity("1");
    }

    @Test
    void createLoan_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> loanService.createLoan("1", "1"));
    }

    @Test
    void createLoan_ShouldThrowBookNotAvaibleException_WhenBookIsNotAvailable() {
        when(userRepository.findById("1")).thenReturn(Optional.of(sampleUser));
        when(bookRepository.findById("1")).thenReturn(Optional.of(sampleBook));
        when(bookService.isBookAvailable("1")).thenReturn(false);

        assertThrows(BookNotAvaibleException.class, () -> loanService.createLoan("1", "1"));
    }

    @Test
    void createLoan_ShouldThrowLoanLimitExceededException_WhenUserHasThreeActiveLoans() {
        when(userRepository.findById("1")).thenReturn(Optional.of(sampleUser));
        when(bookRepository.findById(anyString())).thenReturn(Optional.of(sampleBook));
        when(bookService.isBookAvailable(anyString())).thenReturn(true);
        when(loanRepository.countByUserIdAndStatus("1", LoanStatus.ACTIVE)).thenReturn(3L);

        assertThrows(LoanLimitExceededException.class, () -> loanService.createLoan("1", "1"));
    }

    @Test
    void returnLoan_ShouldReturnBookSuccessfully() {
        when(loanRepository.findFirstByUserIdAndBookIdAndStatus("1", "1", LoanStatus.ACTIVE))
                .thenReturn(Optional.of(sampleLoan));
        when(loanRepository.save(any(Loan.class))).thenReturn(sampleLoan);
        doNothing().when(bookService).increaseAvailableQuantity("1");

        Loan returnedLoan = loanService.returnLoan("1", "1");

        assertEquals(LoanStatus.RETURNED, returnedLoan.getStatus());
        assertNotNull(returnedLoan.getReturnDate());
        verify(bookService, times(1)).increaseAvailableQuantity("1");
    }

    @Test
    void returnLoan_ShouldThrowException_WhenNoActiveLoanFound() {
        when(loanRepository.findFirstByUserIdAndBookIdAndStatus("1", "1", LoanStatus.ACTIVE))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> loanService.returnLoan("1", "1"));
    }

    @Test
    void getAllLoans_ShouldReturnAllLoans() {
        when(loanRepository.findAll()).thenReturn(Collections.singletonList(sampleLoan));

        List<Loan> allLoans = loanService.getAllLoans();

        assertEquals(1, allLoans.size());
        verify(loanRepository, times(1)).findAll();
    }

    @Test
    void createLoan_ShouldThrowException_WhenBookDoesNotExist() {
        when(userRepository.findById("1")).thenReturn(Optional.of(sampleUser));
        when(bookRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> loanService.createLoan("1", "99"));
    }
}
