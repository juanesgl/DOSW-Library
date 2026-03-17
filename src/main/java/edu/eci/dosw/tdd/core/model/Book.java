package edu.eci.dosw.tdd.core.model;

public class Book {

    private String title;
    private String author;
    private String ID;

    public Book(String title, String author, String ID) {
        this.title = title;
        this.author = author;
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getID() {
        return ID;
    }
}