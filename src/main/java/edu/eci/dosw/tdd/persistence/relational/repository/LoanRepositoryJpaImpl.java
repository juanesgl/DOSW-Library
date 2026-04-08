package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.LoanStatus;
import edu.eci.dosw.tdd.core.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.relational.mapper.LoanEntityMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
public class LoanRepositoryJpaImpl implements LoanRepository {

    private final JpaLoanRepository repository;
    private final LoanEntityMapper mapper;

    public LoanRepositoryJpaImpl(JpaLoanRepository repository,
                                 LoanEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Loan save(Loan loan) {
        return mapper.toModel(
                repository.save(mapper.toEntity(loan))
        );
    }

    @Override
    public Optional<Loan> findById(String id) {
        return repository.findById(Long.valueOf(id))
                .map(mapper::toModel);
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(Long.valueOf(id));
    }

    @Override
    public long countByUserIdAndStatus(String userId, LoanStatus status) {
        return repository.countByUserIdAndStatus(Long.valueOf(userId), status);
    }

    @Override
    public Optional<Loan> findFirstByUserIdAndBookIdAndStatus(String userId, String bookId, LoanStatus status) {
        return repository.findFirstByUserIdAndBookIdAndStatus(Long.valueOf(userId), Long.valueOf(bookId), status)
                .map(mapper::toModel);
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        return repository.findByUserId(Long.valueOf(userId))
                .stream()
                .map(mapper::toModel)
                .toList();
    }
}
