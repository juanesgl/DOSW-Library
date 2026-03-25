package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvaibleException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.LoanStatus;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.validator.LoanValidator;
import edu.eci.dosw.tdd.persistence.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.entity.LoanEntity;
import edu.eci.dosw.tdd.persistence.entity.UserEntity;
import edu.eci.dosw.tdd.persistence.mapper.LoanEntityMapper;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;
import edu.eci.dosw.tdd.persistence.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
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
    private LoanEntityMapper loanEntityMapper;

    @Mock
    private LoanValidator loanValidator;

    @Mock
    private BookService bookService;

    @InjectMocks
    private LoanService loanService;

    private UserEntity sampleUserEntity;
    private BookEntity sampleBookEntity;
    private LoanEntity sampleLoanEntity;
    private Loan sampleLoan;

    @BeforeEach
    void setUp() {
        sampleUserEntity = UserEntity.builder().id(1L).name("Test User").username("testuser").password("pass")
                .role("USER").build();
        sampleBookEntity = BookEntity.builder().id(1L).title("Test Book").author("Author").totalQuantity(5)
                .availableQuantity(5).build();
        sampleLoanEntity = LoanEntity.builder().id(1L).user(sampleUserEntity).book(sampleBookEntity)
                .loanDate(LocalDate.now()).status(LoanStatus.ACTIVE).build();
        sampleLoan = Loan.builder().id(1L)
                .user(User.builder().id(1L).name("Test User").build())
                .book(Book.builder().id(1L).title("Test Book").build())
                .loanDate(LocalDate.now()).status(LoanStatus.ACTIVE).build();
    }

    @Test
    void createLoan_ShouldCreateLoanSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUserEntity));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBookEntity));
        when(bookService.isBookAvailable(1L)).thenReturn(true);
        when(loanRepository.countByUserIdAndStatus(1L, LoanStatus.ACTIVE)).thenReturn(0L);
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(sampleLoanEntity);
        when(loanEntityMapper.toModel(any(LoanEntity.class))).thenReturn(sampleLoan);
        doNothing().when(loanValidator).validateLoanCreation(any(), any());
        doNothing().when(bookService).decreaseAvailableQuantity(1L);

        Loan loan = loanService.createLoan(1L, 1L);

        assertNotNull(loan);
        assertEquals(LoanStatus.ACTIVE, loan.getStatus());
        verify(bookService, times(1)).decreaseAvailableQuantity(1L);
    }

    @Test
    void createLoan_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> loanService.createLoan(1L, 1L));
    }

    @Test
    void createLoan_ShouldThrowBookNotAvaibleException_WhenBookIsNotAvailable() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUserEntity));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBookEntity));
        when(bookService.isBookAvailable(1L)).thenReturn(false);

        assertThrows(BookNotAvaibleException.class, () -> loanService.createLoan(1L, 1L));
    }

    @Test
    void createLoan_ShouldThrowLoanLimitExceededException_WhenUserHasThreeActiveLoans() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUserEntity));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(sampleBookEntity));
        when(bookService.isBookAvailable(anyLong())).thenReturn(true);
        when(loanRepository.countByUserIdAndStatus(1L, LoanStatus.ACTIVE)).thenReturn(3L);

        assertThrows(LoanLimitExceededException.class, () -> loanService.createLoan(1L, 1L));
    }

    @Test
    void returnLoan_ShouldReturnBookSuccessfully() {
        when(loanRepository.findFirstByUserIdAndBookIdAndStatus(1L, 1L, LoanStatus.ACTIVE))
                .thenReturn(Optional.of(sampleLoanEntity));
        Loan returnedModel = Loan.builder().id(1L).status(LoanStatus.RETURNED).returnDate(LocalDate.now()).build();
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(sampleLoanEntity);
        when(loanEntityMapper.toModel(any(LoanEntity.class))).thenReturn(returnedModel);
        doNothing().when(bookService).increaseAvailableQuantity(1L);

        Loan returnedLoan = loanService.returnLoan(1L, 1L);

        assertEquals(LoanStatus.RETURNED, returnedLoan.getStatus());
        assertNotNull(returnedLoan.getReturnDate());
        verify(bookService, times(1)).increaseAvailableQuantity(1L);
    }

    @Test
    void returnLoan_ShouldThrowException_WhenNoActiveLoanFound() {
        when(loanRepository.findFirstByUserIdAndBookIdAndStatus(1L, 1L, LoanStatus.ACTIVE))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> loanService.returnLoan(1L, 1L));
    }

    @Test
    void getAllLoans_ShouldReturnAllLoans() {
        when(loanRepository.findAll()).thenReturn(Collections.singletonList(sampleLoanEntity));
        when(loanEntityMapper.toModel(any(LoanEntity.class))).thenReturn(sampleLoan);

        List<Loan> allLoans = loanService.getAllLoans();

        assertEquals(1, allLoans.size());
        verify(loanRepository, times(1)).findAll();
    }
}
