package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.core.model.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookMapperTest {

    private final BookMapper mapper = org.mapstruct.factory.Mappers.getMapper(BookMapper.class);

    @Test
    void toEntity_ShouldMapCorrectly() {
        BookDTO dto = BookDTO.builder().id("1").title("Title").author("Author").totalQuantity(5).availableQuantity(3)
                .build();
        Book entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("1", entity.getId());
        assertEquals("Title", entity.getTitle());
        assertEquals("Author", entity.getAuthor());
        assertEquals(5, entity.getTotalQuantity());
        assertEquals(3, entity.getAvailableQuantity());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDtoIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toDto_ShouldMapCorrectly() {
        Book entity = Book.builder().id("1").title("Title").author("Author").totalQuantity(5).availableQuantity(3)
                .build();
        BookDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals("1", dto.getId());
        assertEquals("Title", dto.getTitle());
        assertEquals("Author", dto.getAuthor());
        assertEquals(5, dto.getTotalQuantity());
        assertEquals(3, dto.getAvailableQuantity());
    }

    @Test
    void toDto_ShouldReturnNull_WhenEntityIsNull() {
        assertNull(mapper.toDto(null));
    }
}
