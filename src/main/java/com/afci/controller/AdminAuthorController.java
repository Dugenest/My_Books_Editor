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
@RequestMapping("/api/admin/author")
@Tag(name = "Author Administration", description = "Author administrative operations")
public class AdminAuthorController {

    @Autowired
    private AuthorService authorService;

    @Operation(summary = "Get author by ID for administration")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Author>> getAuthorById(
            @Parameter(description = "Author ID") @PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @Operation(summary = "Create author with admin privileges")
    @PostMapping
    public ResponseEntity<Author> createAuthor(
            @Parameter(description = "Author details") @Valid @RequestBody Author author) {
        return new ResponseEntity<>(authorService.createAuthor(author), HttpStatus.CREATED);
    }

    @Operation(summary = "Update author with admin privileges")
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(
            @Parameter(description = "Author ID") @PathVariable Long id,
            @Parameter(description = "Author details") @Valid @RequestBody Author author) {
        return ResponseEntity.ok(authorService.updateAuthor(author));
    }

    @Operation(summary = "Delete author with admin privileges")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(
            @Parameter(description = "Author ID") @PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
} 