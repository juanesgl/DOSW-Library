package edu.eci.dosw.tdd.core.exception;

public class BookNotAvaibleException extends Exception {

    public static final String BOOK_NOT_AVAILABLE = "THERE'S NO BOOK AVAILABLE";
    public static final String USER_NOT_FOUND = "THE USER DOESN'T EXIST";
    public static final String BOOK_NOT_FOUND = "WE CAN'T FIND THE BOOK";
    public BookNotAvaibleException(String message) {
        super(message);
    }
}