package edu.eci.dosw.tdd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.dosw.tdd.config.jwt.JwtService;
import edu.eci.dosw.tdd.config.security.CustomUserDetailsService;
import edu.eci.dosw.tdd.config.security.JwtAccessDeniedHandler;
import edu.eci.dosw.tdd.config.security.JwtAuthenticationEntryPoint;
import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.controller.mapper.UserMapper;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    // Security infrastructure mocks
    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockitoBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser_ShouldReturnCreatedUser() throws Exception {
        UserDTO userDTO = UserDTO.builder().name("Test User").username("testuser").password("pass").role("USER")
                .build();
        User user = User.builder().id("1").name("Test User").username("testuser").role("USER").build();
        UserDTO responseDTO = UserDTO.builder().id("1").name("Test User").username("testuser").role("USER").build();

        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        when(userService.registerUser(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        User user = User.builder().id("1").name("Test User").build();
        UserDTO userDTO = UserDTO.builder().id("1").name("Test User").build();

        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Test User"));
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() throws Exception {
        User user = User.builder().id("1").name("Test User").build();
        UserDTO userDTO = UserDTO.builder().id("1").name("Test User").build();

        when(userService.getUserById("1")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void getUserById_ShouldReturnBadRequest_WhenNotExists() throws Exception {
        when(userService.getUserById("99")).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isBadRequest());
    }
}
