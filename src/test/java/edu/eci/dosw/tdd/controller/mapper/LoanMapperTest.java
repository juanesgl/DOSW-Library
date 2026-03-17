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

    private final LoanMapper mapper = new LoanMapper();

    @Test
    void toDto_ShouldMapCorrectly() {
        User user = User.builder().id("U1").build();
        Book book = Book.builder().id("B1").build();
        LocalDate now = LocalDate.now();
        Loan entity = Loan.builder()
                .user(user)
                .book(book)
                .loanDate(now)
                .status(LoanStatus.ACTIVE)
                .build();

        LoanDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals("U1", dto.getUserId());
        assertEquals("B1", dto.getBookId());
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
