package com.afci.controller;

import com.afci.config.TestConfig;
import com.afci.data.Book;
import com.afci.dto.BookDTO;
import com.afci.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(TestConfig.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllBooks_shouldReturnBooksList() throws Exception {
        // Créer des DTOs au lieu d'entités
        BookDTO book1 = new BookDTO();
        book1.setId(1L);
        book1.setTitle("Test Book 1");
        book1.setIsbn("1234567890");
        book1.setDetail("Detail 1");
        book1.setPrice(29.99);
        book1.setStock(10);
        book1.setCategories(new HashSet<>());
        book1.setAuthors(new HashSet<>());

        BookDTO book2 = new BookDTO();
        book2.setId(2L);
        book2.setTitle("Test Book 2");
        book2.setIsbn("0987654321");
        book2.setDetail("Detail 2");
        book2.setPrice(19.99);
        book2.setStock(5);
        book2.setCategories(new HashSet<>());
        book2.setAuthors(new HashSet<>());

        Pageable pageable = PageRequest.of(0, 10);
        Page<BookDTO> bookPage = new PageImpl<>(Arrays.asList(book1, book2));

        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(bookPage);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("Test Book 1"))
                .andExpect(jsonPath("$.content[1].title").value("Test Book 2"));
    }

    @Test
    void getBookById_whenBookExists_shouldReturnBook() throws Exception {
        BookDTO book = new BookDTO();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setIsbn("1234567890");
        book.setDetail("Detail");
        book.setPrice(29.99);
        book.setStock(10);
        book.setCategories(new HashSet<>());
        book.setAuthors(new HashSet<>());

        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    void createBook_withValidData_shouldReturnCreatedBook() throws Exception {
        // Pour créer un livre, on utilise toujours l'entité Book, pas le DTO
        Book bookToCreate = new Book("Complete Book Title", "978-3-16-148410-0", "Complete details", 39.99);
        bookToCreate.setStock(10);
        bookToCreate.setCategories(new HashSet<>());
        bookToCreate.setOrders(new HashSet<>());
        bookToCreate.setBasketBooks(new HashSet<>());

        Book createdBook = new Book("Complete Book Title", "978-3-16-148410-0", "Complete details", 39.99);
        createdBook.setId(1L);
        createdBook.setStock(10);
        createdBook.setCategories(new HashSet<>());
        createdBook.setOrders(new HashSet<>());
        createdBook.setBasketBooks(new HashSet<>());

        when(bookService.createBook(any(Book.class))).thenReturn(createdBook);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Complete Book Title"));
    }

    @Test
    void updateBook_withValidData_shouldReturnUpdatedBook() throws Exception {
        // Pour mettre à jour un livre, on utilise toujours l'entité Book, pas le DTO
        Book bookToUpdate = new Book("Updated Book", "1234567890", "Updated details", 49.99);
        bookToUpdate.setId(1L);
        bookToUpdate.setStock(15);
        bookToUpdate.setCategories(new HashSet<>());
        bookToUpdate.setOrders(new HashSet<>());
        bookToUpdate.setBasketBooks(new HashSet<>());

        when(bookService.updateBook(any(Book.class))).thenReturn(bookToUpdate);

        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"));
    }

    @Test
    void deleteBook_shouldReturnNoContent() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createBook_withInvalidTitleOnly_shouldReturnBadRequest() throws Exception {
        Book invalidBook = new Book();
        invalidBook.setTitle(""); // Invalid title

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBook)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBook_withInvalidPrice_shouldReturnBadRequest() throws Exception {
        Book invalidBook = new Book("Test Book", "1234567890", "Detail", -10.0); // Invalid price
        invalidBook.setId(1L);

        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBook)))
                .andExpect(status().isBadRequest());
    }
}