package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.repository.UserRepository;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserValidator userValidator;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder().id("1").name("Test User").username("testuser").password("pass").role("USER").build();
    }

    @Test
    void registerUser_ShouldRegisterUserSuccessfully() {
        doNothing().when(userValidator).validate(any(User.class));
        when(passwordEncoder.encode("pass")).thenReturn("encodedpass");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        User registeredUser = userService.registerUser(sampleUser);

        assertNotNull(registeredUser.getId());
        assertEquals("Test User", registeredUser.getName());
        verify(userValidator, times(1)).validate(sampleUser);
        verify(passwordEncoder, times(1)).encode("pass");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenValidatorFails() {
        doThrow(new IllegalArgumentException("El nombre del usuario no puede estar vacío"))
                .when(userValidator).validate(any());

        User invalidUser = User.builder().name("").build();

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(invalidUser));
        verify(userValidator, times(1)).validate(invalidUser);
        verify(userRepository, never()).save(any());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(sampleUser));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById("1")).thenReturn(Optional.of(sampleUser));

        Optional<User> foundUser = userService.getUserById("1");

        assertTrue(foundUser.isPresent());
        assertEquals("Test User", foundUser.get().getName());
    }

    @Test
    void getUserById_ShouldReturnEmpty_WhenUserDoesNotExist() {
        when(userRepository.findById("99")).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserById("99");

        assertFalse(foundUser.isPresent());
    }
}
