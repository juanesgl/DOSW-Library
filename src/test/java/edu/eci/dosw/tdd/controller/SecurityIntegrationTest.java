package edu.eci.dosw.tdd.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.context.ActiveProfiles("relational")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenNoTokenProvided_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenInvalidTokenProvided_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/books")
                .header("Authorization", "Bearer invalid-token.signature.payload"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "userTest", roles = { "USER" })
    void whenUserAttemptsLibrarianEndpoint_thenForbidden() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "librarianTest", roles = { "LIBRARIAN" })
    void whenLibrarianAttemptsLibrarianEndpoint_thenOk() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }
}
