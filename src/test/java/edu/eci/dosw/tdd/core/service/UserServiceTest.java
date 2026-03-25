package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import edu.eci.dosw.tdd.persistence.entity.UserEntity;
import edu.eci.dosw.tdd.persistence.mapper.UserEntityMapper;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private UserEntityMapper userEntityMapper;

    @InjectMocks
    private UserService userService;

    private User sampleUser;
    private UserEntity sampleEntity;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder().name("Test User").username("testuser").password("pass").role("USER").build();
        sampleEntity = UserEntity.builder().id(1L).name("Test User").username("testuser").password("pass").role("USER")
                .build();
    }

    @Test
    void registerUser_ShouldRegisterUserSuccessfully() {
        doNothing().when(userValidator).validate(any(User.class));
        when(userEntityMapper.toEntity(any(User.class))).thenReturn(sampleEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(sampleEntity);
        when(userEntityMapper.toModel(any(UserEntity.class))).thenReturn(
                User.builder().id(1L).name("Test User").username("testuser").role("USER").build());

        User registeredUser = userService.registerUser(sampleUser);

        assertNotNull(registeredUser.getId());
        assertEquals("Test User", registeredUser.getName());
        verify(userValidator, times(1)).validate(sampleUser);
        verify(userRepository, times(1)).save(any(UserEntity.class));
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
        when(userRepository.findAll()).thenReturn(Collections.singletonList(sampleEntity));
        when(userEntityMapper.toModel(any(UserEntity.class))).thenReturn(sampleUser);

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
        when(userEntityMapper.toModel(sampleEntity)).thenReturn(sampleUser);

        Optional<User> foundUser = userService.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("Test User", foundUser.get().getName());
    }

    @Test
    void getUserById_ShouldReturnEmpty_WhenUserDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserById(99L);

        assertFalse(foundUser.isPresent());
    }
}
