package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.config.jwt.JwtService;
import edu.eci.dosw.tdd.config.security.CustomUserDetailsService;
import edu.eci.dosw.tdd.config.security.JwtAccessDeniedHandler;
import edu.eci.dosw.tdd.config.security.JwtAuthenticationEntryPoint;
import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.controller.mapper.LoanMapper;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.service.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;

    @MockitoBean
    private LoanMapper loanMapper;

    // Security infrastructure mocks
    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockitoBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Test
    void createLoan_ShouldReturnCreatedLoan() throws Exception {
        Loan loan = new Loan();
        LoanDTO responseDTO = LoanDTO.builder().userId("1").bookId("1").status("ACTIVE").build();

        when(loanService.createLoan("1", "1")).thenReturn(loan);
        when(loanMapper.toDto(loan)).thenReturn(responseDTO);

        mockMvc.perform(post("/loans")
                .param("userId", "1")
                .param("bookId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.bookId").value("1"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void returnLoan_ShouldReturnUpdatedLoan() throws Exception {
        Loan loan = new Loan();
        LoanDTO responseDTO = LoanDTO.builder().userId("1").bookId("1").status("RETURNED").build();

        when(loanService.returnLoan("1", "1")).thenReturn(loan);
        when(loanMapper.toDto(loan)).thenReturn(responseDTO);

        mockMvc.perform(put("/loans/return")
                .param("userId", "1")
                .param("bookId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RETURNED"));
    }

    @Test
    void getAllLoans_ShouldReturnList() throws Exception {
        Loan loan = new Loan();
        LoanDTO responseDTO = LoanDTO.builder().userId("1").bookId("1").build();

        when(loanService.getAllLoans()).thenReturn(Collections.singletonList(loan));
        when(loanMapper.toDto(loan)).thenReturn(responseDTO);

        mockMvc.perform(get("/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("1"));
    }
}
