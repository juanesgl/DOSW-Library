package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.core.exception.BookNotAvaibleException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleBookNotAvailable_ShouldReturnConflict() {
        BookNotAvaibleException ex = new BookNotAvaibleException("Libro no disponible");

        ResponseEntity<Map<String, String>> response = handler.handleBookNotAvailable(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict", response.getBody().get("error"));
        assertEquals("Libro no disponible", response.getBody().get("message"));
    }

    @Test
    void handleLoanLimitExceeded_ShouldReturnForbidden() {
        LoanLimitExceededException ex = new LoanLimitExceededException("Límite alcanzado");

        ResponseEntity<Map<String, String>> response = handler.handleLoanLimitExceeded(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Forbidden", response.getBody().get("error"));
        assertEquals("Límite alcanzado", response.getBody().get("message"));
    }

    @Test
    void handleUserNotFound_ShouldReturnNotFound() {
        UserNotFoundException ex = new UserNotFoundException("Usuario no encontrado");

        ResponseEntity<Map<String, String>> response = handler.handleUserNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody().get("error"));
        assertEquals("Usuario no encontrado", response.getBody().get("message"));
    }

    @Test
    void handleIllegalArgument_ShouldReturnBadRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("Argumento inválido");

        ResponseEntity<Map<String, String>> response = handler.handleIllegalArgument(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad Request", response.getBody().get("error"));
        assertEquals("Argumento inválido", response.getBody().get("message"));
    }
}
