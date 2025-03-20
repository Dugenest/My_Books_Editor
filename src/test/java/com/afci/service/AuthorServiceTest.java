package com.afci.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.afci.data.Author;
import com.afci.repository.AuthorRepository;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthorService authorService;

    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author("user123", "password", "email@test.com", "Doe", "John", "French");
        author.setId(1L);
        lenient().when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }

    @Test
    void testGetAllAuthors() {
        List<Author> authors = Arrays.asList(author, new Author("user456", "password", "email2@test.com", "Smith", "Jane", "American"));
        when(authorRepository.findAll()).thenReturn(authors);

        List<Author> result = authorService.getAllAuthors();
        assertEquals(2, result.size());
    }

    @Test
    void testGetAuthorById() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        Optional<Author> result = authorService.getAuthorById(1L);
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    void testCreateAuthor() {
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        Author result = authorService.createAuthor(author);
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testUpdateAuthor() {
        Author existingAuthor = new Author("user123", "password", "email@test.com", "Doe", "John", "French");
        existingAuthor.setId(1L);
        when(authorRepository.existsById(1L)).thenReturn(true);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(existingAuthor);

        Author updatedAuthor = new Author("user123", "newpassword", "newemail@test.com", "NewDoe", "NewJohn", "French");
        updatedAuthor.setId(1L);
        Author result = authorService.updateAuthor(updatedAuthor);
        assertEquals("NewDoe", result.getLastName());
    }

    @Test
    void testDeleteAuthor() {
        Author existingAuthor = new Author("user123", "password", "email@test.com", "Doe", "John", "French");
        existingAuthor.setId(1L);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(existingAuthor));

        assertDoesNotThrow(() -> authorService.deleteAuthor(1L));
    }

    @Test
    void testFindByName() {
        List<Author> authors = Arrays.asList(author);
        when(authorRepository.findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase("John", "John")).thenReturn(authors);

        List<Author> result = authorService.findByName("John");
        assertEquals(1, result.size());
    }
}
