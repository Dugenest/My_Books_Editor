package com.afci.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.afci.data.Book;
import com.afci.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
@Validated
@Tag(name = "Book Management", description = "APIs pour la gestion des livres")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "Récupérer les nouvelles parutions")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nouvelles parutions récupérées avec succès"),
            @ApiResponse(responseCode = "404", description = "Aucune nouvelle parution trouvée")
    })
    @GetMapping("/new-releases")
    public ResponseEntity<?> getNewReleases(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<Book> newReleases = bookService.getNewReleases(limit);
            return ResponseEntity.ok(newReleases);
        } catch (Exception e) {
            // Retourner une liste vide en cas d'erreur temporaire
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @Operation(summary = "Récupérer les livres populaires")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livres populaires récupérés avec succès"),
            @ApiResponse(responseCode = "404", description = "Aucun livre populaire trouvé")
    })
    @GetMapping("/popular")
    public ResponseEntity<?> getPopularBooks(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<Book> popularBooks = bookService.getPopularBooks(limit);
            return ResponseEntity.ok(popularBooks);
        } catch (Exception e) {
            // Retourner une liste vide en cas d'erreur temporaire
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @Operation(summary = "Récupérer tous les livres avec pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des livres récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Aucun livre trouvé")
    })
    @GetMapping
    public ResponseEntity<Page<Book>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Récupérer un livre par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livre trouvé"),
            @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Créer un nouveau livre")
    @ApiResponse(responseCode = "201", description = "Livre créé avec succès")
    @PostMapping
    public ResponseEntity<Book> createBook(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Détails du livre") @Valid @RequestBody Book book) {
        return new ResponseEntity<>(bookService.createBook(book), HttpStatus.CREATED);
    }

    @Operation(summary = "Mettre à jour un livre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livre mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @Parameter(description = "ID du livre") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Détails du livre") @Valid @RequestBody Book book) {
        book.setId(id);
        return ResponseEntity.ok(bookService.updateBook(book));
    }

    @Operation(summary = "Supprimer un livre")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livre supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID du livre") @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}