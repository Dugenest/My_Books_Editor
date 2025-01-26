package com.afci.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.afci.data.Book;
import com.afci.exception.BookNotFoundException;
import com.afci.exception.InvalidBookDataException;
import com.afci.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/books")
@Tag(name = "Book Management", description = "APIs for managing books")
public class BookController {

	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping
	@Operation(summary = "List all books", description = "Fetches all books available in the system.")
	@ApiResponse(responseCode = "200", description = "Successfully retrieved list of books")
	public ResponseEntity<List<Book>> getAllBooks() {
		List<Book> books = bookService.getAllBooks();
		if (books.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(books);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get a book by ID", description = "Fetches a single book using its ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Book found"),
		@ApiResponse(responseCode = "404", description = "Book not found")
	})
	public ResponseEntity<Book> getBookById(@PathVariable Long id) {
		try {
			Book book = bookService.getBookById(id);
			return ResponseEntity.ok(book);
		} catch (BookNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@PostMapping
	@Operation(summary = "Add a new book", description = "Creates a new book record.")
	@ApiResponse(responseCode = "201", description = "Book successfully created")
	public ResponseEntity<?> addBook(@RequestBody Book book) {
		try {
			if (book.getIdBook() != null && bookService.existsById(book.getIdBook())) {
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body("Book with ID " + book.getIdBook() + " already exists.");
			}
			Book savedBook = bookService.addBook(book);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
		} catch (InvalidBookDataException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred: " + ex.getMessage());
		}
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update a book", description = "Updates an existing book's information.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Book updated successfully"),
		@ApiResponse(responseCode = "404", description = "Book not found")
	})
	public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
		try {
			Book updatedBook = bookService.updateBook(id, book);
			return ResponseEntity.ok(updatedBook);
		} catch (BookNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a book", description = "Removes a book by its ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Book deleted successfully"),
		@ApiResponse(responseCode = "404", description = "Book not found")
	})
	public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
		try {
			bookService.deleteBook(id);
			return ResponseEntity.noContent().build();
		} catch (BookNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
}
