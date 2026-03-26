package edu.eci.dosw.tdd.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LoanDTO {
    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El ID del libro es obligatorio")
    private Long bookId;
    private LocalDate loanDate;
    private String status;
    private LocalDate returnDate;
}