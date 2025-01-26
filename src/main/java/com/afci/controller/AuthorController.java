package com.afci.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afci.data.Author;
import com.afci.exception.AuthorNotFoundException;
import com.afci.service.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/authors")
@Tag(name = "Author Management", description = "APIs for managing authors")
public class AuthorController {

	private final AuthorService authorService;

	public AuthorController(AuthorService authorService) {
		this.authorService = authorService;
	}

	@GetMapping
	@Operation(summary = "List all authors", description = "Fetches all authors available in the system.")
	@ApiResponse(responseCode = "200", description = "Successfully retrieved list of authors")
	public ResponseEntity<List<Author>> getAllAuthors() {
		List<Author> authors = authorService.getAllAuthors();
		return ResponseEntity.ok(authors);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get an author by ID", description = "Fetches a single author using its ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Author found"),
		@ApiResponse(responseCode = "404", description = "Author not found")
	})
	public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
		try {
			Author author = authorService.getAuthorById(id);
			return ResponseEntity.ok(author);
		} catch (AuthorNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@PostMapping
	@Operation(summary = "Add a new author", description = "Creates a new author record.")
	@ApiResponse(responseCode = "201", description = "Author successfully created")
	public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
		Author createdAuthor = authorService.createAuthor(author);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update an author", description = "Updates an existing author's information.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Author updated successfully"),
		@ApiResponse(responseCode = "404", description = "Author not found")
	})
	public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author updatedAuthor) {
		try {
			Author updated = authorService.updateAuthor(id, updatedAuthor);
			return ResponseEntity.ok(updated);
		} catch (AuthorNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete an author", description = "Removes an author by its ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Author deleted successfully"),
		@ApiResponse(responseCode = "404", description = "Author not found")
	})
	public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
		boolean deleted = authorService.deleteAuthor(id);
		if (deleted) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
}
