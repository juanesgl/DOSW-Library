package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.core.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    void toEntity_ShouldMapCorrectly() {
        UserDTO dto = UserDTO.builder().id(1L).name("John").username("john123").password("pass").role("USER").build();
        User entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("John", entity.getName());
        assertEquals("john123", entity.getUsername());
        assertEquals("pass", entity.getPassword());
        assertEquals("USER", entity.getRole());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDtoIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toDto_ShouldMapCorrectly() {
        User entity = User.builder().id(1L).name("John").username("john123").password("pass").role("USER").build();
        UserDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getName());
        assertEquals("john123", dto.getUsername());
        assertEquals("USER", dto.getRole());
        // password should not be exposed in DTO
        assertNull(dto.getPassword());
    }

    @Test
    void toDto_ShouldReturnNull_WhenEntityIsNull() {
        assertNull(mapper.toDto(null));
    }
}
