package edu.eci.dosw.tdd.persistence.relational.mapper;

import edu.eci.dosw.tdd.core.model.*;
import edu.eci.dosw.tdd.persistence.relational.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.relational.entity.LoanEntity;
import edu.eci.dosw.tdd.persistence.relational.entity.UserEntity;
import edu.eci.dosw.tdd.persistence.relational.mapper.BookEntityMapper;
import edu.eci.dosw.tdd.persistence.relational.mapper.LoanEntityMapper;
import edu.eci.dosw.tdd.persistence.relational.mapper.UserEntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;

@ExtendWith(MockitoExtension.class)
class LoanEntityMapperTest {

    @Mock
    private UserEntityMapper userEntityMapper;

    @Mock
    private BookEntityMapper bookEntityMapper;

    private LoanEntityMapper loanEntityMapper;

    @BeforeEach
    void setUp() {
        loanEntityMapper = org.mapstruct.factory.Mappers.getMapper(LoanEntityMapper.class);
        org.springframework.test.util.ReflectionTestUtils.setField(loanEntityMapper, "userEntityMapper", userEntityMapper);
        org.springframework.test.util.ReflectionTestUtils.setField(loanEntityMapper, "bookEntityMapper", bookEntityMapper);
    }

    @Test
    void toModel_ShouldMapAllFields() {
        UserEntity userEntity = UserEntity.builder().id(1L).name("Test User").build();
        BookEntity bookEntity = BookEntity.builder().id(2L).title("Test Book").build();
        LocalDate loanDate = LocalDate.of(2026, 3, 1);
        LocalDate returnDate = LocalDate.of(2026, 3, 15);

        LoanEntity entity = LoanEntity.builder()
                .id(10L)
                .user(userEntity)
                .book(bookEntity)
                .loanDate(loanDate)
                .status(LoanStatus.ACTIVE)
                .returnDate(returnDate)
                .build();

        User mappedUser = User.builder().id("1").name("Test User").build();
        Book mappedBook = Book.builder().id("2").title("Test Book").build();

        when(userEntityMapper.toModel(userEntity)).thenReturn(mappedUser);
        when(bookEntityMapper.toModel(bookEntity)).thenReturn(mappedBook);

        Loan model = loanEntityMapper.toModel(entity);

        assertNotNull(model);
        assertEquals("10", model.getId());
        assertEquals(mappedUser, model.getUser());
        assertEquals(mappedBook, model.getBook());
        assertEquals(loanDate, model.getLoanDate());
        assertEquals(LoanStatus.ACTIVE, model.getStatus());
        assertEquals(returnDate, model.getReturnDate());
    }

    @Test
    void toModel_ShouldReturnNull_WhenEntityIsNull() {
        assertNull(loanEntityMapper.toModel(null));
    }

    @Test
    void toEntity_ShouldMapAllFields() {
        User user = User.builder().id("1").name("Test User").build();
        Book book = Book.builder().id("2").title("Test Book").build();
        LocalDate loanDate = LocalDate.of(2026, 3, 1);
        LocalDate returnDate = LocalDate.of(2026, 3, 15);

        Loan model = Loan.builder()
                .id("10")
                .user(user)
                .book(book)
                .loanDate(loanDate)
                .status(LoanStatus.RETURNED)
                .returnDate(returnDate)
                .build();

        UserEntity mappedUserEntity = UserEntity.builder().id(1L).name("Test User").build();
        BookEntity mappedBookEntity = BookEntity.builder().id(2L).title("Test Book").build();

        when(userEntityMapper.toEntity(user)).thenReturn(mappedUserEntity);
        when(bookEntityMapper.toEntity(book)).thenReturn(mappedBookEntity);

        LoanEntity entity = loanEntityMapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(10L, entity.getId());
        assertEquals(mappedUserEntity, entity.getUser());
        assertEquals(mappedBookEntity, entity.getBook());
        assertEquals(loanDate, entity.getLoanDate());
        assertEquals(LoanStatus.RETURNED, entity.getStatus());
        assertEquals(returnDate, entity.getReturnDate());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenModelIsNull() {
        assertNull(loanEntityMapper.toEntity(null));
    }
}
