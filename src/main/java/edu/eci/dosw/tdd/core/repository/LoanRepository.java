package edu.eci.dosw.tdd.core.repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.LoanStatus;

import java.util.List;
import java.util.Optional;

public interface LoanRepository {
    Loan save(Loan loan);
    Optional<Loan> findById(String id);
    List<Loan> findAll();
    void delete(String id);
    
    long countByUserIdAndStatus(String userId, LoanStatus status);
    Optional<Loan> findFirstByUserIdAndBookIdAndStatus(String userId, String bookId, LoanStatus status);
    List<Loan> findByUserId(String userId);
}
