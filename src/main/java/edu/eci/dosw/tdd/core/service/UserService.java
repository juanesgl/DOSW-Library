package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserValidator userValidator;
    private final List<User> users = new ArrayList<>();

    public User registerUser(User user) {
        // 1. Validamos las reglas de negocio
        userValidator.validate(user);

        // 2. Generamos un ID si no lo tiene
        if (user.getId() == null) {
            user.setId(IdGeneratorUtil.generateId());
        }

        users.add(user);
        return user;
    }

    public List<User> getAllUsers() {
        return users;
    }

    public Optional<User> getUserById(String id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }
}