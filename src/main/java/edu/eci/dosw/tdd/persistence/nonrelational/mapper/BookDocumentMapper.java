package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.nonrelational.document.BookDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookDocumentMapper {
    Book toDomain(BookDocument document);

    @Mapping(target = "isbn", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "publicationType", ignore = true)
    @Mapping(target = "publicationDate", ignore = true)
    @Mapping(target = "added_at", ignore = true)
    @Mapping(target = "metadata", ignore = true)
    @Mapping(target = "availability", ignore = true)
    BookDocument toDocument(Book domain);
}
