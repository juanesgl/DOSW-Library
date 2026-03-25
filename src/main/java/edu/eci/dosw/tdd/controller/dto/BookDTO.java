package edu.eci.dosw.tdd.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private Integer totalQuantity;
    private Integer availableQuantity;
}