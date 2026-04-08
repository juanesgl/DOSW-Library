package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.config.jwt.JwtService;
import edu.eci.dosw.tdd.config.security.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.validation.Valid;
import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.controller.mapper.UserMapper;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario", description = "Registra un usuario en el sistema")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User createdUser = userService.registerUser(user);

        UserDTO responseDto = userMapper.toDto(createdUser);

        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(createdUser.getUsername());
            String token = jwtService.generateToken(userDetails, createdUser.getId());
            responseDto.setToken(token);
        } catch (Exception e) {
            // Token generation failed, user was still created successfully
            responseDto.setToken("User created - please login via /auth/login to get token");
        }

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve la lista de usuarios registrados")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @Operation(summary = "Obtener usuario por ID", description = "Busca un usuario por su identificador único")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}