package edu.eci.dosw.tdd.persistence.nonrelational.document.Loan;

import lombok.Data;
import java.util.Date;

@Data
public class History {
    private String status;
    private Date executed_at;
}
