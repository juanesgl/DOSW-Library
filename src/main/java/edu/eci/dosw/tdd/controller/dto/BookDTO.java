package edu.eci.dosw.tdd.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class BookDTO {
    private String id;

    @NotBlank(message = "El título es obligatorio")
    private String title;

    @NotBlank(message = "El autor es obligatorio")
    private String author;

    @NotNull(message = "La cantidad total es obligatoria")
    @Min(value = 0, message = "La cantidad total no puede ser negativa")
    private Integer totalQuantity;

    private Integer availableQuantity;
}