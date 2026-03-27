package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.core.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDTO dto) {
        if (dto == null)
            return null;

        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .role(dto.getRole())
                .build();
    }

    public UserDTO toDto(User entity) {
        if (entity == null)
            return null;

        return UserDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .username(entity.getUsername())
                .role(entity.getRole())
                .build();
    }
}