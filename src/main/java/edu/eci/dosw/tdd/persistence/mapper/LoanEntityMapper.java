package edu.eci.dosw.tdd.persistence.mapper;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.entity.LoanEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoanEntityMapper {

    private final UserEntityMapper userEntityMapper;
    private final BookEntityMapper bookEntityMapper;

    public Loan toModel(LoanEntity entity) {
        if (entity == null)
            return null;
        return Loan.builder()
                .id(entity.getId())
                .user(userEntityMapper.toModel(entity.getUser()))
                .book(bookEntityMapper.toModel(entity.getBook()))
                .loanDate(entity.getLoanDate())
                .status(entity.getStatus())
                .returnDate(entity.getReturnDate())
                .build();
    }

    public LoanEntity toEntity(Loan model) {
        if (model == null)
            return null;
        return LoanEntity.builder()
                .id(model.getId())
                .user(userEntityMapper.toEntity(model.getUser()))
                .book(bookEntityMapper.toEntity(model.getBook()))
                .loanDate(model.getLoanDate())
                .status(model.getStatus())
                .returnDate(model.getReturnDate())
                .build();
    }
}
