package edu.eci.dosw.tdd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.dosw.tdd.config.jwt.JwtService;
import edu.eci.dosw.tdd.config.security.CustomUserDetailsService;
import edu.eci.dosw.tdd.config.security.JwtAccessDeniedHandler;
import edu.eci.dosw.tdd.config.security.JwtAuthenticationEntryPoint;
import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private BookService bookService;

        @MockitoBean
        private BookMapper bookMapper;

        // Security infrastructure mocks (required by SecurityConfig &
        // JwtAuthenticationFilter)
        @MockitoBean
        private JwtService jwtService;

        @MockitoBean
        private CustomUserDetailsService customUserDetailsService;

        @MockitoBean
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        @MockitoBean
        private JwtAccessDeniedHandler jwtAccessDeniedHandler;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void addBook_ShouldReturnCreatedBook() throws Exception {
                BookDTO bookDTO = BookDTO.builder().title("Clean Code").author("Uncle Bob").totalQuantity(5)
                                .availableQuantity(5).build();
                Book book = Book.builder().id("1").title("Clean Code").author("Uncle Bob").totalQuantity(5)
                                .availableQuantity(5)
                                .build();
                BookDTO responseDTO = BookDTO.builder().id("1").title("Clean Code").author("Uncle Bob").totalQuantity(5)
                                .availableQuantity(5).build();

                when(bookMapper.toEntity(any(BookDTO.class))).thenReturn(book);
                when(bookService.addBook(any(Book.class))).thenReturn(book);
                when(bookMapper.toDto(any(Book.class))).thenReturn(responseDTO);

                mockMvc.perform(post("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value("1"))
                                .andExpect(jsonPath("$.title").value("Clean Code"));
        }

        @Test
        void getAllBooks_ShouldReturnInventory() throws Exception {
                Book book = Book.builder().id("1").title("Clean Code").author("Uncle Bob").totalQuantity(5)
                                .availableQuantity(5)
                                .build();
                BookDTO bookDTO = BookDTO.builder().id("1").title("Clean Code").author("Uncle Bob").totalQuantity(5)
                                .availableQuantity(5).build();

                when(bookService.getAllBooks()).thenReturn(Collections.singletonList(book));
                when(bookMapper.toDto(book)).thenReturn(bookDTO);

                mockMvc.perform(get("/books"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value("1"))
                                .andExpect(jsonPath("$[0].totalQuantity").value(5));
        }

        @Test
        void getBookById_ShouldReturnBook_WhenExists() throws Exception {
                Book book = Book.builder().id("1").title("Clean Code").build();
                BookDTO bookDTO = BookDTO.builder().id("1").title("Clean Code").build();

                when(bookService.getBookById("1")).thenReturn(Optional.of(book));
                when(bookMapper.toDto(book)).thenReturn(bookDTO);

                mockMvc.perform(get("/books/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value("1"));
        }

    @Test
    void getBookById_ShouldReturnBadRequest_WhenBookNotFound() throws Exception {
        when(bookService.getBookById("99")).thenReturn(Optional.empty());

        mockMvc.perform(get("/books/99"))
                .andExpect(status().isBadRequest());
    }
}
