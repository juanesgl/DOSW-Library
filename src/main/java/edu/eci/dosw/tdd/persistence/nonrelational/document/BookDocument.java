package edu.eci.dosw.tdd.persistence.nonrelational.document;

import edu.eci.dosw.tdd.persistence.nonrelational.document.Book.Availability;
import edu.eci.dosw.tdd.persistence.nonrelational.document.Book.Metadata;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "books")
public class BookDocument {
    @Id
    private String id;
    private String title;
    private String author;
    private Integer totalQuantity;
    private Integer availableQuantity;

    // Extended Mongo-only fields
    private String isbn;
    private List<String> categories;
    private String publicationType;
    private Date publicationDate;
    private Date added_at;

    private Metadata metadata;
    private Availability availability;

}
