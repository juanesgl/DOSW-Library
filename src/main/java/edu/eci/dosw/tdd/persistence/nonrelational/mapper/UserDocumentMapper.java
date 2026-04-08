package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDocumentMapper {
    User toDomain(UserDocument document);
    UserDocument toDocument(User domain);
}
