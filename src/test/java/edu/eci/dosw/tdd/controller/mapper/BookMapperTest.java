package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.core.model.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookMapperTest {

    private final BookMapper mapper = new BookMapper();

    @Test
    void toEntity_ShouldMapCorrectly() {
        BookDTO dto = BookDTO.builder().id("B1").title("Title").author("Author").build();
        Book entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("B1", entity.getId());
        assertEquals("Title", entity.getTitle());
        assertEquals("Author", entity.getAuthor());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDtoIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toDto_ShouldMapCorrectly() {
        Book entity = Book.builder().id("B1").title("Title").author("Author").build();
        BookDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals("B1", dto.getId());
        assertEquals("Title", dto.getTitle());
        assertEquals("Author", dto.getAuthor());
    }

    @Test
    void toDto_ShouldReturnNull_WhenEntityIsNull() {
        assertNull(mapper.toDto(null));
    }
}
