package edu.eci.dosw.tdd.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserDTO {
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    private String role;
    private String token;
}