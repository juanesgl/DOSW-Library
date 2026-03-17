package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.core.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    void toEntity_ShouldMapCorrectly() {
        UserDTO dto = UserDTO.builder().id("1").name("John").build();
        User entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("1", entity.getId());
        assertEquals("John", entity.getName());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDtoIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toDto_ShouldMapCorrectly() {
        User entity = User.builder().id("1").name("John").build();
        UserDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals("1", dto.getId());
        assertEquals("John", dto.getName());
    }

    @Test
    void toDto_ShouldReturnNull_WhenEntityIsNull() {
        assertNull(mapper.toDto(null));
    }
}
