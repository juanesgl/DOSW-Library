package edu.eci.dosw.tdd.persistence.relational.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.relational.entity.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BookEntityMapper {

    @Mapping(target = "id", source = "id", qualifiedByName = "longToString")
    Book toModel(BookEntity entity);

    @Mapping(target = "id", source = "id", qualifiedByName = "stringToLong")
    BookEntity toEntity(Book model);

    @Named("longToString")
    default String longToString(Long id) {
        return id != null ? String.valueOf(id) : null;
    }

    @Named("stringToLong")
    default Long stringToLong(String id) {
        return id != null && !id.isEmpty() ? Long.valueOf(id) : null;
    }
}
