package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.core.model.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {

    public LoanDTO toDto(Loan entity) {
        if (entity == null)
            return null;

        return LoanDTO.builder()
                .id(entity.getId())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .bookId(entity.getBook() != null ? entity.getBook().getId() : null)
                .loanDate(entity.getLoanDate())
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .returnDate(entity.getReturnDate())
                .build();
    }
}