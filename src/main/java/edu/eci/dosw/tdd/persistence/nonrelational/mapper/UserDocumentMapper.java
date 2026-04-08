package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDocumentMapper {
    User toDomain(UserDocument document);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "membership", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UserDocument toDocument(User domain);
}
