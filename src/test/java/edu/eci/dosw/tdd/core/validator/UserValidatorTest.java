package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private final UserValidator validator = new UserValidator();

    @Test
    void validate_ShouldDoNothing_WhenUserIsValid() {
        User validUser = User.builder().name("John Doe").build();
        assertDoesNotThrow(() -> validator.validate(validUser));
    }

    @Test
    void validate_ShouldThrowException_WhenUserIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(null));
        assertEquals("El usuario no puede ser nulo", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowException_WhenNameIsNullOrEmpty() {
        User userWithEmptyName = User.builder().name("").build();
        User userWithNullName = User.builder().name(null).build();

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> validator.validate(userWithEmptyName));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> validator.validate(userWithNullName));

        assertEquals("El nombre del usuario no puede estar vacío", ex1.getMessage());
        assertEquals("El nombre del usuario no puede estar vacío", ex2.getMessage());
    }
}
