package edu.eci.dosw.tdd.persistence.nonrelational.document;

import edu.eci.dosw.tdd.persistence.nonrelational.document.Loan.History;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "loans")
public class LoanDocument {
    @Id
    private String id;

    private String userId;
    private String bookId;
    private Date loanDate;
    private Date returnDate;
    private String status;

    private List<History> history;

}
