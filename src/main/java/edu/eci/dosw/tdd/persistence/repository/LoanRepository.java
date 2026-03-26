package edu.eci.dosw.tdd.persistence.repository;

import edu.eci.dosw.tdd.core.model.LoanStatus;
import edu.eci.dosw.tdd.persistence.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
    long countByUserIdAndStatus(Long userId, LoanStatus status);

    Optional<LoanEntity> findFirstByUserIdAndBookIdAndStatus(Long userId, Long bookId, LoanStatus status);

    List<LoanEntity> findByUserId(Long userId);
}
