package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import edu.eci.dosw.tdd.persistence.entity.UserEntity;
import edu.eci.dosw.tdd.persistence.mapper.UserEntityMapper;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserValidator userValidator;
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        userValidator.validate(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserEntity entity = userEntityMapper.toEntity(user);
        UserEntity saved = userRepository.save(entity);
        return userEntityMapper.toModel(saved);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userEntityMapper::toModel)
                .collect(Collectors.toList());
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userEntityMapper::toModel);
    }
}