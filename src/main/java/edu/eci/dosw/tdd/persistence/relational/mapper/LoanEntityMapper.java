package edu.eci.dosw.tdd.persistence.relational.mapper;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.relational.entity.LoanEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserEntityMapper.class, BookEntityMapper.class}, injectionStrategy = org.mapstruct.InjectionStrategy.FIELD)
public interface LoanEntityMapper {
    Loan toModel(LoanEntity entity);
    LoanEntity toEntity(Loan model);
}
