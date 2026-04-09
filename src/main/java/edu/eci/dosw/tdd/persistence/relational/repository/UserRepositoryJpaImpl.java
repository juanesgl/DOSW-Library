package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.repository.UserRepository;
import edu.eci.dosw.tdd.persistence.relational.mapper.UserEntityMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
public class UserRepositoryJpaImpl implements UserRepository {

    private final JpaUserRepository repository;
    private final UserEntityMapper mapper;

    public UserRepositoryJpaImpl(JpaUserRepository repository,
                                 UserEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        return mapper.toModel(
                repository.save(mapper.toEntity(user))
        );
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(Long.valueOf(id))
                .map(mapper::toModel);
    }

    @Override
    public List<User> findAll() {
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
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username)
                .map(mapper::toModel);
    }
}
