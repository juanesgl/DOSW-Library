package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.LoanStatus;
import edu.eci.dosw.tdd.core.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.nonrelational.mapper.LoanDocumentMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("mongo")
public class LoanRepositoryMongoImpl implements LoanRepository {

    private final LoanMongoRepository repository;
    private final LoanDocumentMapper mapper;

    public LoanRepositoryMongoImpl(LoanMongoRepository repository,
                                   LoanDocumentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Loan save(Loan loan) {
        return mapper.toDomain(
                repository.save(mapper.toDocument(loan))
        );
    }

    @Override
    public Optional<Loan> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public long countByUserIdAndStatus(String userId, LoanStatus status) {
        return repository.countByUserIdAndStatus(userId, status.name());
    }

    @Override
    public Optional<Loan> findFirstByUserIdAndBookIdAndStatus(String userId, String bookId, LoanStatus status) {
        return repository.findFirstByUserIdAndBookIdAndStatus(userId, bookId, status.name())
                .map(mapper::toDomain);
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
