package com.afci.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afci.data.Author;
import com.afci.service.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Author Management", description = "APIs pour la gestion des auteurs")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Operation(summary = "Récupérer tous les auteurs")
    @GetMapping
    public ResponseEntity<Page<Author>> getAllAuthors(Pageable pageable) {
        return ResponseEntity.ok(authorService.getAllAuthors(pageable));
    }

    @Operation(summary = "Récupérer un auteur par ID")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Author>> getAuthorById(
        @Parameter(description = "ID de l'auteur") @PathVariable Long id
    ) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @Operation(summary = "Créer un nouvel auteur")
    @PostMapping
    public ResponseEntity<Author> createAuthor(
        @Parameter(description = "Détails de l'auteur") @Valid @RequestBody Author author
    ) {
        return new ResponseEntity<>(authorService.createAuthor(author), HttpStatus.CREATED);
    }

    @Operation(summary = "Mettre à jour un auteur")
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(
        @Parameter(description = "ID de l'auteur") @PathVariable Long id,
        @Parameter(description = "Détails de l'auteur") @Valid @RequestBody Author author
    ) {
        author.setId(id);
        return ResponseEntity.ok(authorService.updateAuthor(author));
    }

    @Operation(summary = "Supprimer un auteur")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(
        @Parameter(description = "ID de l'auteur") @PathVariable Long id
    ) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}