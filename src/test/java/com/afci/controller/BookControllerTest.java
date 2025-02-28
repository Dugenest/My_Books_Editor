package com.afci.controller;

import com.afci.data.Book;
import com.afci.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false) // Désactive la sécurité dans les tests
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllBooks_shouldReturnBooksList() throws Exception {
        // Arrange
        Book book1 = new Book("Test Book 1", "1234567890", "Detail 1", 29.99);
        Book book2 = new Book("Test Book 2", "0987654321", "Detail 2", 19.99);
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        // Act & Assert
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Test Book 1"))
                .andExpect(jsonPath("$[1].title").value("Test Book 2"));
    }

    @Test
    void getBookById_whenBookExists_shouldReturnBook() throws Exception {
        // Arrange
        Book book = new Book("Test Book", "1234567890", "Detail", 29.99);
        book.setId(1L);
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        // Act & Assert
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
void createBook_withAllRequiredFields_shouldReturnCreatedBook() throws Exception {
    // Créez une instance complète avec TOUS les champs obligatoires
    Book bookToCreate = new Book("Complete Book Title", "978-3-16-148410-0", "Complete details", 39.99);
    bookToCreate.setStock(10);
    
    // Si d'autres associations sont obligatoires, configurez-les aussi
    
    Book createdBook = new Book("Complete Book Title", "978-3-16-148410-0", "Complete details", 39.99);
    createdBook.setId(1L);
    createdBook.setStock(10);
    
    when(bookService.createBook(any(Book.class))).thenReturn(createdBook);
    
    // Imprimez le JSON avant l'envoi
    String requestJson = objectMapper.writeValueAsString(bookToCreate);
    System.out.println("JSON de requête: " + requestJson);
    
    // Act & Assert
    mockMvc.perform(post("/api/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists());
}

    @Test
    void createBook_withValidData_shouldReturnCreatedBook() throws Exception {
        // Arrange - Book avec toutes les données requises
        Book bookToCreate = new Book();
        bookToCreate.setTitle("New Book"); // Titre obligatoire
        bookToCreate.setISBN("978-3-16-148410-0"); // ISBN valide
        bookToCreate.setDetail("New Detail");
        bookToCreate.setPrice(39.99); // Prix positif obligatoire
        bookToCreate.setStock(10); // Stock positif

        Book createdBook = new Book();
        createdBook.setId(1L);
        createdBook.setTitle("New Book");
        createdBook.setISBN("978-3-16-148410-0");
        createdBook.setDetail("New Detail");
        createdBook.setPrice(39.99);
        createdBook.setStock(10);

        when(bookService.createBook(any(Book.class))).thenReturn(createdBook);

        // Act & Assert
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookToCreate)))
                .andDo(print()) // Pour voir la réponse
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("New Book"));
    }

    @Test
    void createBook_withInvalidTitleOnly_shouldReturnBadRequest() throws Exception {
        // Arrange : Livre sans titre uniquement
        Book invalidBook = new Book();
        invalidBook.setTitle(""); // Titre vide = invalide
        invalidBook.setPrice(10.0); // Prix valide pour éviter cette erreur
        invalidBook.setStock(5);

        // Act & Assert
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBook)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Le titre est obligatoire"));
    }

    @Test
    void updateBook_withValidData_shouldReturnUpdatedBook() throws Exception {
        // Arrange
        Book bookToUpdate = new Book();
        bookToUpdate.setId(1L);
        bookToUpdate.setTitle("Updated Title"); // Titre obligatoire
        bookToUpdate.setISBN("978-3-16-148410-0"); // ISBN valide
        bookToUpdate.setDetail("Updated Detail");
        bookToUpdate.setPrice(49.99); // Prix positif obligatoire
        bookToUpdate.setStock(15);

        when(bookService.updateBook(any(Book.class))).thenReturn(bookToUpdate);

        // Act & Assert
        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookToUpdate)))
                .andDo(print()) // Pour voir la réponse
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void updateBook_withInvalidPrice_shouldReturnBadRequest() throws Exception {
        // Test pour l'erreur de prix uniquement
        Book invalidBook = new Book();
        invalidBook.setId(1L);
        invalidBook.setTitle("Titre valide"); // Titre valide pour éviter l'erreur de titre
        invalidBook.setPrice(-5.0); // Prix négatif = invalide

        // Act & Assert avec vérification plus souple
        MvcResult result = mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBook)))
                .andDo(print()) // Pour voir la réponse complète
                .andExpect(status().isBadRequest())
                .andReturn();

        // Vérification que la réponse contient une erreur liée au prix
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("price") || content.contains("Prix"),
                "La réponse ne contient pas d'erreur de prix: " + content);
    }

    @Test
    void deleteBook_shouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(bookService).deleteBook(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService).deleteBook(1L);
    }

    @Test
    void debugValidationErrors() throws Exception {
        // Livre avec plusieurs violations potentielles
        Book debugBook = new Book();
        // Ne définissez pas de titre
        debugBook.setPrice(-10.0); // Prix négatif
        debugBook.setISBN("invalid-isbn"); // ISBN invalide

        // Juste pour voir la réponse complète
        MvcResult result = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(debugBook)))
                .andDo(print())
                .andReturn();

        System.out.println("DEBUG - Erreurs de validation: " +
                result.getResponse().getContentAsString());
    }

    @Test
    void debugJsonSerialization() throws Exception {
        Book book = new Book();
        book.setTitle("Test Title");
        book.setPrice(29.99);

        String json = objectMapper.writeValueAsString(book);
        System.out.println("JSON sérialisé: " + json);

        Book deserializedBook = objectMapper.readValue(json, Book.class);
        System.out.println("Titre après désérialisation: " + deserializedBook.getTitle());
    }
}