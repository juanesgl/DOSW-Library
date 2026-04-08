package edu.eci.dosw.tdd.persistence.relational.mapper;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.relational.entity.LoanEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserEntityMapper.class, BookEntityMapper.class})
public interface LoanEntityMapper {

    @Mapping(target = "id", expression = "java(entity.getId() != null ? String.valueOf(entity.getId()) : null)")
    Loan toModel(LoanEntity entity);

    @Mapping(target = "id", expression = "java(model.getId() != null && !model.getId().isEmpty() ? Long.valueOf(model.getId()) : null)")
    LoanEntity toEntity(Loan model);
}
