package edu.eci.dosw.tdd.core.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class BookNotAvaibleException extends RuntimeException {
    public BookNotAvaibleException(String message) {
        super(message);
    }
}