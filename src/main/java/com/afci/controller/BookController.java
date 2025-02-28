package com.afci.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afci.data.Book;
import com.afci.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
@Validated  // ✅ Assure que la validation fonctionne sur les contrôleurs
@Tag(name = "Book Management", description = "APIs pour la gestion des livres")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "Récupérer tous les livres")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste des livres récupérée avec succès"),
        @ApiResponse(responseCode = "404", description = "Aucun livre trouvé")
    })
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @Operation(summary = "Récupérer un livre par son ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Livre trouvé"),
        @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Book>> getBookById(
        @Parameter(description = "ID du livre") @PathVariable Long id
    ) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @Operation(summary = "Créer un nouveau livre")
    @ApiResponse(responseCode = "201", description = "Livre créé avec succès")
    @PostMapping
    public ResponseEntity<Book> createBook(
        @Parameter(description = "Détails du livre") 
        @Valid @RequestBody Book book
    ) {
        return new ResponseEntity<>(bookService.createBook(book), HttpStatus.CREATED);
    }

    @Operation(summary = "Mettre à jour un livre")
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
        @Parameter(description = "ID du livre") @PathVariable Long id,
        @Parameter(description = "Détails du livre") @Valid @RequestBody Book book
    ) {
        book.setId(id);
        return ResponseEntity.ok(bookService.updateBook(book));
    }

    @Operation(summary = "Supprimer un livre")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
        @Parameter(description = "ID du livre") @PathVariable Long id
    ) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}