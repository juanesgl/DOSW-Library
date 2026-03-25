package edu.eci.dosw.tdd.persistence.mapper;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityMapperTest {

    private UserEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserEntityMapper();
    }

    @Test
    void toModel_ShouldMapAllFields() {
        UserEntity entity = UserEntity.builder()
                .id(1L)
                .name("Juan")
                .username("juanuser")
                .password("secret123")
                .role("ADMIN")
                .build();

        User model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals(1L, model.getId());
        assertEquals("Juan", model.getName());
        assertEquals("juanuser", model.getUsername());
        assertEquals("secret123", model.getPassword());
        assertEquals("ADMIN", model.getRole());
    }

    @Test
    void toModel_ShouldReturnNull_WhenEntityIsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toEntity_ShouldMapAllFields() {
        User model = User.builder()
                .id(1L)
                .name("Juan")
                .username("juanuser")
                .password("secret123")
                .role("ADMIN")
                .build();

        UserEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Juan", entity.getName());
        assertEquals("juanuser", entity.getUsername());
        assertEquals("secret123", entity.getPassword());
        assertEquals("ADMIN", entity.getRole());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenModelIsNull() {
        assertNull(mapper.toEntity(null));
    }
}
