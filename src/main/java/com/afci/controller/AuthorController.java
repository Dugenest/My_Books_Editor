package com.afci.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.afci.data.Author;
import com.afci.service.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Author Management", description = "Author operations")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Operation(summary = "Get all authors")
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @Operation(summary = "Récupérer un auteur par ID")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Author>> getAuthorById(
            @Parameter(description = "ID de l'auteur") @PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @Operation(summary = "Créer un nouvel auteur")
    @PostMapping
    public ResponseEntity<Author> createAuthor(
            @Parameter(description = "Détails de l'auteur") @Valid @RequestBody Author author) {
        return new ResponseEntity<>(authorService.createAuthor(author), HttpStatus.CREATED);
    }

    @Operation(summary = "Mettre à jour un auteur")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthor(
            @Parameter(description = "ID de l'auteur") @PathVariable Long id,
            @Parameter(description = "Détails de l'auteur") @RequestBody Author author) {
        try {
            author.setId(id);
            Author updatedAuthor = authorService.updateAuthor(author);
            return ResponseEntity.ok(updatedAuthor);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Erreur lors de la mise à jour",
                            "message", e.getMessage()));
        }
    }

    @Operation(summary = "Mettre à jour un auteur avec méthode simple")
    @PostMapping("/{id}/simple-update")
    public ResponseEntity<?> simpleUpdateAuthor(
            @Parameter(description = "ID de l'auteur") @PathVariable Long id,
            @Parameter(description = "Détails de l'auteur") @RequestBody Author author) {
        try {
            author.setId(id);
            Author updatedAuthor = authorService.updateAuthor(author);
            return ResponseEntity.ok(updatedAuthor);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Erreur lors de la mise à jour simple",
                            "message", e.getMessage()));
        }
    }

    @Operation(summary = "Mettre à jour un auteur avec bypass des validations")
    @PostMapping("/{id}/update-bypass")
    public ResponseEntity<?> bypassUpdateAuthor(
            @Parameter(description = "ID de l'auteur") @PathVariable Long id,
            @Parameter(description = "Détails de l'auteur") @RequestBody Author author) {
        try {
            author.setId(id);
            Author updatedAuthor = authorService.updateAuthor(author);
            return ResponseEntity.ok(updatedAuthor);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Erreur lors de la mise à jour avec bypass",
                            "message", e.getMessage()));
        }
    }

    @Operation(summary = "Supprimer un auteur")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthor(
            @Parameter(description = "ID de l'auteur") @PathVariable Long id) {
        try {
            authorService.deleteAuthor(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Retourner une réponse d'erreur avec le message d'exception
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Erreur lors de la suppression",
                            "message", e.getMessage()));
        }
    }

    @Operation(summary = "Supprimer un auteur de force")
    @DeleteMapping("/force-delete/{id}")
    public ResponseEntity<?> forceDeleteAuthor(
            @Parameter(description = "ID de l'auteur") @PathVariable Long id) {
        try {
            // Supprimer l'auteur avec la méthode de suppression en cascade
            authorService.forceDeleteAuthor(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Erreur lors de la suppression forcée",
                            "message", e.getMessage()));
        }
    }

    @Operation(summary = "Rechercher des auteurs par nom")
    @GetMapping("/search")
    public ResponseEntity<List<Author>> findByName(
            @Parameter(description = "Nom à rechercher") @RequestParam String name) {
        return ResponseEntity.ok(authorService.findByName(name));
    }
}