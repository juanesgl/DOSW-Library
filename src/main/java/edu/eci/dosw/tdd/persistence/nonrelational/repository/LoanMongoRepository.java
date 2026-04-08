package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LoanMongoRepository extends MongoRepository<LoanDocument, String> {

    List<LoanDocument> findByUserId(String userId);

    List<LoanDocument> findByStatus(String status);
    
    long countByUserIdAndStatus(String userId, String status);

    Optional<LoanDocument> findFirstByUserIdAndBookIdAndStatus(String userId, String bookId, String status);
}
