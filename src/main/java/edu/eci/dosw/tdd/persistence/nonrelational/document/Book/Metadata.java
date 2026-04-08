package edu.eci.dosw.tdd.persistence.nonrelational.document.Book;

import lombok.Data;

@Data
public class Metadata {
    private Integer pages;
    private String language;
    private String publisher;
}
