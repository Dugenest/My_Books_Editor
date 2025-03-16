package com.afci.service;

import com.afci.data.Book;
import com.afci.repository.BookRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book("Test Book", "1234567890", "Test Detail", 29.99);
        testBook.setId(1L);
    }

    @Test
    void getAllBooks_shouldReturnAllBooks() {
        // Arrange
        List<Book> expectedBooks = Arrays.asList(
                testBook,
                new Book("Test Book 2", "0987654321", "Test Detail 2", 19.99));
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> expectedPage = new PageImpl<>(expectedBooks);
        when(bookRepository.findAll(pageable)).thenReturn(expectedPage);

        // Act
        Page<Book> actualPage = bookService.getAllBooks(pageable);

        // Assert
        assertThat(actualPage.getContent()).hasSize(2);
        assertThat(actualPage.getContent()).isEqualTo(expectedBooks);
        verify(bookRepository).findAll(pageable);
    }

    @Test
    void getBookById_whenBookExists_shouldReturnBook() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        // Act
        Optional<Book> result = bookService.getBookById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Test Book");
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookById_whenBookDoesNotExist_shouldReturnEmpty() {
        // Arrange
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Book> result = bookService.getBookById(99L);

        // Assert
        assertThat(result).isEmpty();
        verify(bookRepository).findById(99L);
    }

    @Test
    void createBook_shouldReturnSavedBook() {
        // Arrange
        Book bookToCreate = new Book("New Book", "1111111111", "New Detail", 39.99);
        when(bookRepository.save(any(Book.class))).thenReturn(bookToCreate);

        // Act
        Book createdBook = bookService.createBook(bookToCreate);

        // Assert
        assertThat(createdBook).isNotNull();
        assertThat(createdBook.getTitle()).isEqualTo("New Book");
        verify(bookRepository).save(bookToCreate);
    }

    @Test
    void updateBook_whenBookExists_shouldUpdateAndReturnBook() {
        // Arrange
        Book bookToUpdate = testBook;
        bookToUpdate.setTitle("Updated Title");
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.save(any(Book.class))).thenReturn(bookToUpdate);

        // Act
        Book updatedBook = bookService.updateBook(bookToUpdate);

        // Assert
        assertThat(updatedBook.getTitle()).isEqualTo("Updated Title");
        verify(bookRepository).existsById(1L);
        verify(bookRepository).save(bookToUpdate);
    }

    @Test
    void updateBook_whenBookDoesNotExist_shouldThrowException() {
        // Arrange
        Book nonExistentBook = testBook;
        when(bookRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> bookService.updateBook(nonExistentBook))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Livre non trouvé");
        verify(bookRepository).existsById(1L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void findBooksByTitle_shouldReturnMatchingBooks() {
        // Arrange
        String searchTitle = "Test";
        List<Book> expectedBooks = Arrays.asList(testBook);
        when(bookRepository.findByTitleContainingIgnoreCase(searchTitle))
                .thenReturn(expectedBooks);

        // Act
        List<Book> foundBooks = bookService.findBooksByTitle(searchTitle);

        // Assert
        assertThat(foundBooks).hasSize(1);
        assertThat(foundBooks.get(0).getTitle()).contains(searchTitle);
        verify(bookRepository).findByTitleContainingIgnoreCase(searchTitle);
    }

    @Test
    void findBooksByCategory_shouldReturnBooksInCategory() {
        // Arrange
        String category = "Fiction";
        List<Book> expectedBooks = Arrays.asList(testBook);
        when(bookRepository.findByCategory_Name(category)).thenReturn(expectedBooks);

        // Act
        List<Book> foundBooks = bookService.findBooksByCategory(category);

        // Assert
        assertThat(foundBooks).hasSize(1);
        verify(bookRepository).findByCategory_Name(category);
    }

    @Test
    void findByAuthorName_shouldReturnBooksFromAuthor() {
        // Arrange
        String lastName = "Doe";
        String firstName = "John";
        List<Book> expectedBooks = Arrays.asList(testBook);
        when(bookRepository.findByAuthor_LastNameContainingIgnoreCaseOrAuthor_FirstNameContainingIgnoreCase(
                lastName, firstName)).thenReturn(expectedBooks);

        // Act
        List<Book> foundBooks = bookService.findByAuthorName(lastName, firstName);

        // Assert
        assertThat(foundBooks).hasSize(1);
        verify(bookRepository)
                .findByAuthor_LastNameContainingIgnoreCaseOrAuthor_FirstNameContainingIgnoreCase(
                        lastName, firstName);
    }

    @Test
    void deleteBook_shouldCallRepositoryDelete() {
        // Arrange
        Long bookId = 1L;

        // Act
        bookService.deleteBook(bookId);

        // Assert
        verify(bookRepository).deleteById(bookId);
    }
}