package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserDocumentMapper.class, BookDocumentMapper.class}, injectionStrategy = org.mapstruct.InjectionStrategy.FIELD)
public interface LoanDocumentMapper {
    Loan toDomain(LoanDocument document);
    LoanDocument toDocument(Loan domain);
}
