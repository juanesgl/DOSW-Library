package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.LoanStatus;
import edu.eci.dosw.tdd.core.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LoanMapperTest {

    private final LoanMapper mapper = org.mapstruct.factory.Mappers.getMapper(LoanMapper.class);

    @Test
    void toDto_ShouldMapCorrectly() {
        User user = User.builder().id(1L).build();
        Book book = Book.builder().id(1L).build();
        LocalDate now = LocalDate.now();
        Loan entity = Loan.builder()
                .id(1L)
                .user(user)
                .book(book)
                .loanDate(now)
                .status(LoanStatus.ACTIVE)
                .build();

        LoanDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getUserId());
        assertEquals(1L, dto.getBookId());
        assertEquals(now, dto.getLoanDate());
        assertEquals("ACTIVE", dto.getStatus());
    }

    @Test
    void toDto_ShouldHandleNullsInEntity() {
        Loan entity = Loan.builder().build();
        LoanDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertNull(dto.getUserId());
        assertNull(dto.getBookId());
        assertNull(dto.getStatus());
    }

    @Test
    void toDto_ShouldReturnNull_WhenEntityIsNull() {
        assertNull(mapper.toDto(null));
    }
}
