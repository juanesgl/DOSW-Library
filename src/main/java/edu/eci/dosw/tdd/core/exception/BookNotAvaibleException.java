package edu.eci.dosw.tdd.core.exception;

public class BookNotAvaibleException extends RuntimeException {
    public BookNotAvaibleException(String message) {
        super(message);
    }
}