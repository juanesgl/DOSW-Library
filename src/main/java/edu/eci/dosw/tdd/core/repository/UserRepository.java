package edu.eci.dosw.tdd.core.repository;

import edu.eci.dosw.tdd.core.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(String id);
    List<User> findAll();
    void delete(String id);
    Optional<User> findByUsername(String username);
}
