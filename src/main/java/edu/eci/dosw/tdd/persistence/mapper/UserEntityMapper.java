package edu.eci.dosw.tdd.persistence.mapper;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    public User toModel(UserEntity entity) {
        if (entity == null)
            return null;
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .role(entity.getRole())
                .build();
    }

    public UserEntity toEntity(User model) {
        if (model == null)
            return null;
        return UserEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .username(model.getUsername())
                .password(model.getPassword())
                .role(model.getRole())
                .build();
    }
}
