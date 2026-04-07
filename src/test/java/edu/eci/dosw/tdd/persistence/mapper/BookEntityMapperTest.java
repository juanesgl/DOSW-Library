package edu.eci.dosw.tdd.persistence.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.relational.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.relational.mapper.BookEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookEntityMapperTest {

    private BookEntityMapper mapper = org.mapstruct.factory.Mappers.getMapper(BookEntityMapper.class);

    @BeforeEach
    void setUp() {
    }

    @Test
    void toModel_ShouldMapAllFields() {
        BookEntity entity = BookEntity.builder()
                .id(1L)
                .title("Clean Code")
                .author("Uncle Bob")
                .totalQuantity(5)
                .availableQuantity(3)
                .build();

        Book model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals(1L, model.getId());
        assertEquals("Clean Code", model.getTitle());
        assertEquals("Uncle Bob", model.getAuthor());
        assertEquals(5, model.getTotalQuantity());
        assertEquals(3, model.getAvailableQuantity());
    }

    @Test
    void toModel_ShouldReturnNull_WhenEntityIsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toEntity_ShouldMapAllFields() {
        Book model = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Uncle Bob")
                .totalQuantity(5)
                .availableQuantity(3)
                .build();

        BookEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Clean Code", entity.getTitle());
        assertEquals("Uncle Bob", entity.getAuthor());
        assertEquals(5, entity.getTotalQuantity());
        assertEquals(3, entity.getAvailableQuantity());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenModelIsNull() {
        assertNull(mapper.toEntity(null));
    }
}
