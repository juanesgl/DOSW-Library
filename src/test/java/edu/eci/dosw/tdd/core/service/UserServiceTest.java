package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .name("Test User")
                .build();
    }

    @Test
    void registerUser_ShouldRegisterUserSuccessfully() {
        // Arrange
        doNothing().when(userValidator).validate(any(User.class));

        // Act
        User registeredUser = userService.registerUser(sampleUser);

        // Assert
        assertNotNull(registeredUser.getId());
        assertEquals("Test User", registeredUser.getName());
        verify(userValidator, times(1)).validate(sampleUser);
        assertTrue(userService.getAllUsers().contains(registeredUser));
    }

    @Test
    void registerUser_ShouldThrowException_WhenValidatorFails() {
        // Arrange
        doThrow(new IllegalArgumentException("El nombre del usuario no puede estar vacío"))
                .when(userValidator).validate(any());

        User invalidUser = User.builder().name("").build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(invalidUser));
        verify(userValidator, times(1)).validate(invalidUser);
        assertFalse(userService.getAllUsers().contains(invalidUser));
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        // Arrange
        doNothing().when(userValidator).validate(any());
        userService.registerUser(sampleUser);

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertEquals(1, users.size());
        assertEquals(sampleUser, users.get(0));
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        // Arrange
        doNothing().when(userValidator).validate(any());
        User registeredUser = userService.registerUser(sampleUser);
        String userId = registeredUser.getId();

        // Act
        Optional<User> foundUser = userService.getUserById(userId);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(registeredUser, foundUser.get());
    }

    @Test
    void getUserById_ShouldReturnEmpty_WhenUserDoesNotExist() {
        // Act
        Optional<User> foundUser = userService.getUserById("non-existent-id");

        // Assert
        assertFalse(foundUser.isPresent());
    }
}
