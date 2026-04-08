package edu.eci.dosw.tdd.persistence.nonrelational.document.Book;

import lombok.Data;

@Data
public class Availability {
    private String status;
    private Integer total_copies;
    private Integer available_copies;
    private Integer borrowed_copies;
}
