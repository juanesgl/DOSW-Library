package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.LoanStatus;
import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface LoanDocumentMapper {

    @Mapping(target = "book.id", source = "bookId")
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToLoanStatus")
    Loan toDomain(LoanDocument document);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "status", source = "status", qualifiedByName = "loanStatusToString")
    @Mapping(target = "history", ignore = true)
    LoanDocument toDocument(Loan domain);

    @Named("stringToLoanStatus")
    default LoanStatus stringToLoanStatus(String status) {
        return status != null ? LoanStatus.valueOf(status) : null;
    }

    @Named("loanStatusToString")
    default String loanStatusToString(LoanStatus status) {
        return status != null ? status.name() : null;
    }
}
