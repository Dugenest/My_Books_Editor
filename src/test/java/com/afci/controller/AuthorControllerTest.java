package com.afci.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.afci.data.Author;
import com.afci.service.AuthorService;

@ExtendWith(MockitoExtension.class)
public class AuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    @Test
    void testGetAllAuthors() {
        List<Author> authors = Arrays.asList(
                new Author("user123", "password", "email@test.com", "Doe", "John", "French"),
                new Author("user456", "password", "email2@test.com", "Smith", "Jane", "American"));
        when(authorService.getAllAuthors()).thenReturn(authors);

        ResponseEntity<List<Author>> response = authorController.getAllAuthors();
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetAuthorById() {
        Author author = new Author("user123", "password", "email@test.com", "Doe", "John", "French");
        when(authorService.getAuthorById(1L)).thenReturn(Optional.of(author));

        ResponseEntity<Optional<Author>> response = authorController.getAuthorById(1L);
        assertTrue(response.getBody().isPresent());
        assertEquals("John", response.getBody().get().getFirstName());
    }

    @Test
    void testCreateAuthor() {
        Author author = new Author("user123", "password", "email@test.com", "Doe", "John", "French");
        when(authorService.createAuthor(any())).thenReturn(author);

        ResponseEntity<Author> response = authorController.createAuthor(author);
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
    }

    @Test
    void testUpdateAuthor() {
        Author author = new Author("user123", "password", "email@test.com", "Doe", "John", "French");
        when(authorService.updateAuthor(any())).thenReturn(author);

        ResponseEntity<?> response = authorController.updateAuthor(1L, author);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof Author);
        assertEquals("Doe", ((Author) response.getBody()).getLastName());
    }

    @Test
    void testSimpleUpdateAuthor() {
        Author author = new Author("user123", "password", "email@test.com", "Doe", "John", "French");
        when(authorService.updateAuthor(any())).thenReturn(author);

        ResponseEntity<?> response = authorController.simpleUpdateAuthor(1L, author);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof Author);
        assertEquals("Doe", ((Author) response.getBody()).getLastName());
    }

    @Test
    void testBypassUpdateAuthor() {
        Author author = new Author("user123", "password", "email@test.com", "Doe", "John", "French");
        when(authorService.updateAuthor(any())).thenReturn(author);

        ResponseEntity<?> response = authorController.bypassUpdateAuthor(1L, author);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof Author);
        assertEquals("Doe", ((Author) response.getBody()).getLastName());
    }

    @Test
    void testDeleteAuthor() {
        doNothing().when(authorService).deleteAuthor(1L);

        ResponseEntity<?> response = authorController.deleteAuthor(1L);
        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void testForceDeleteAuthor() {
        doNothing().when(authorService).forceDeleteAuthor(1L);

        ResponseEntity<?> response = authorController.forceDeleteAuthor(1L);
        assertEquals(204, response.getStatusCode().value());
    }
}