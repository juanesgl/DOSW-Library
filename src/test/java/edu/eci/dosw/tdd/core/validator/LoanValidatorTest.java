package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanValidatorTest {

    private final LoanValidator validator = new LoanValidator();

    @Test
    void validateLoanCreation_ShouldDoNothing_WhenInputsAreValid() {
        User user = User.builder().id(1L).build();
        Book book = Book.builder().id(101L).build();
        assertDoesNotThrow(() -> validator.validateLoanCreation(user, book));
    }

    @Test
    void validateLoanCreation_ShouldThrowException_WhenUserIsNull() {
        Book book = Book.builder().id(101L).build();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validateLoanCreation(null, book));
        assertEquals("El usuario es requerido para el préstamo", exception.getMessage());
    }

    @Test
    void validateLoanCreation_ShouldThrowException_WhenBookIsNull() {
        User user = User.builder().id(1L).build();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validateLoanCreation(user, null));
        assertEquals("El libro es requerido para el préstamo", exception.getMessage());
    }
}