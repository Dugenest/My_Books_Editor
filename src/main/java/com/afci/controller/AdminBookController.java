package com.afci.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.afci.data.Author;
import com.afci.data.Book;
import com.afci.dto.AuthorDTO;
import com.afci.dto.BookDTO;
import com.afci.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/book")
@Tag(name = "Book Administration", description = "Book administrative operations")
public class AdminBookController {

    private static final Logger logger = LoggerFactory.getLogger(AdminBookController.class);

    @Autowired
    private BookService bookService;

    @Operation(summary = "Get book by ID for administration")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@Parameter(description = "Book ID") @PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookService.getBookById(id));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du livre " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de la récupération du livre : " + e.getMessage()));
        }
    }

    @Operation(summary = "Create book with admin privileges")
    @PostMapping
    public ResponseEntity<?> createBook(@Parameter(description = "Book details") @Valid @RequestBody Book book) {
        try {
            Book createdBook = bookService.createBook(book);
            return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur lors de la création du livre", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de la création du livre : " + e.getMessage()));
        }
    }

    @Operation(summary = "Update book with admin privileges")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
            @Parameter(description = "Book ID") @PathVariable Long id,
            @Parameter(description = "Book details") @Valid @RequestBody Book book) {
        try {
            book.setId(id); // Assurer que l'ID est correctement défini
            Book updatedBook = bookService.updateBook(book);
            return ResponseEntity.ok(updatedBook);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du livre " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de la mise à jour du livre : " + e.getMessage()));
        }
    }

    @Operation(summary = "Delete book with admin privileges")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@Parameter(description = "Book ID") @PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du livre " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de la suppression du livre : " + e.getMessage()));
        }
    }

    @Operation(summary = "Get authors by book for administration")
    @GetMapping("/{id}/authors")
    public ResponseEntity<?> getAuthorsByBook(@Parameter(description = "Book ID") @PathVariable Long id) {
        try {
            Set<AuthorDTO> authors = bookService.getAuthorsByBook(id);
            return ResponseEntity.ok(authors);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des auteurs pour le livre " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de la récupération des auteurs : " + e.getMessage()));
        }
    }

    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
} 