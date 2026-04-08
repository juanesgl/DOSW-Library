package edu.eci.dosw.tdd.persistence.relational.mapper;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.relational.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    @Mapping(target = "id", source = "id", qualifiedByName = "longToString")
    User toModel(UserEntity entity);

    @Mapping(target = "id", source = "id", qualifiedByName = "stringToLong")
    UserEntity toEntity(User model);

    @Named("longToString")
    default String longToString(Long id) {
        return id != null ? String.valueOf(id) : null;
    }

    @Named("stringToLong")
    default Long stringToLong(String id) {
        return id != null && !id.isEmpty() ? Long.valueOf(id) : null;
    }
}
