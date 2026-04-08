package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.nonrelational.document.BookDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookDocumentMapper {
    Book toDomain(BookDocument document);
    BookDocument toDocument(Book domain);
}
