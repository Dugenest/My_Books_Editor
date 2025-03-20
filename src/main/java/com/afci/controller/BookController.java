package com.afci.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.afci.data.Author;
import com.afci.data.Book;
import com.afci.data.Category;
import com.afci.dto.AuthorDTO;
import com.afci.dto.BookDTO;
import com.afci.dto.CategoryDTO;
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
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE })
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

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
            List<BookDTO> newReleases = bookService.getNewReleases(limit);
            return ResponseEntity.ok(newReleases);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des nouvelles parutions", e);
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
            List<BookDTO> popularBooks = bookService.getPopularBooks(limit);
            return ResponseEntity.ok(popularBooks);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des livres populaires", e);
            // Retourner une liste vide en cas d'erreur temporaire
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @Operation(summary = "Get all books")
    @GetMapping
    public ResponseEntity<Page<BookDTO>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            Page<BookDTO> books = bookService.getAllBooks(pageable);
            return ResponseEntity.ok(books);
        } catch (Exception ex) {
            logger.error("Erreur lors de la récupération des livres", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Récupérer un livre par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livre trouvé"),
            @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@Parameter(description = "Book ID") @PathVariable Long id) {
        try {
            Optional<BookDTO> book = bookService.getBookById(id);
            return book.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du livre " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de la récupération du livre : " + e.getMessage()));
        }
    }

    @Operation(summary = "Créer un nouveau livre")
    @ApiResponse(responseCode = "201", description = "Livre créé avec succès")
    @PostMapping
    public ResponseEntity<?> createBook(@Parameter(description = "Book details") @Valid @RequestBody Book book) {
        try {
            Book savedBook = bookService.createBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
        } catch (Exception e) {
            logger.error("Erreur lors de la création du livre", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de la création du livre : " + e.getMessage()));
        }
    }

    @Operation(summary = "Mettre à jour un livre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livre mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@Parameter(description = "Book ID") @PathVariable Long id,
            @Parameter(description = "Book details") @Valid @RequestBody Book book) {
        try {
            book.setId(id);
            Book updatedBook = bookService.updateBook(book);
            return ResponseEntity.ok(updatedBook);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du livre " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de la mise à jour du livre : " + e.getMessage()));
        }
    }

    @Operation(summary = "Supprimer un livre")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livre supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    })
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

    @Operation(summary = "Rechercher des livres par titre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livres trouvés avec succès"),
            @ApiResponse(responseCode = "404", description = "Aucun livre trouvé")
    })
    @GetMapping("/search/title")
    public ResponseEntity<?> searchBooksByTitle(@RequestParam String title) {
        try {
            List<BookDTO> books = bookService.findBooksByTitle(title);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de livres par titre", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de la recherche de livres : " + e.getMessage()));
        }
    }

    @Operation(summary = "Rechercher des livres par auteur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livres trouvés avec succès"),
            @ApiResponse(responseCode = "404", description = "Aucun livre trouvé")
    })
    @GetMapping("/search/author")
    public ResponseEntity<?> searchBooksByAuthor(
            @RequestParam(required = false, defaultValue = "") String lastName,
            @RequestParam(required = false, defaultValue = "") String firstName) {
        try {
            List<BookDTO> books = bookService.findByAuthorName(lastName, firstName);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de livres par auteur", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de la recherche de livres : " + e.getMessage()));
        }
    }

    @Operation(summary = "Rechercher des livres par catégorie")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livres trouvés avec succès"),
            @ApiResponse(responseCode = "404", description = "Aucun livre trouvé")
    })
    @GetMapping("/search/category")
    public ResponseEntity<?> searchBooksByCategory(@RequestParam String categoryName) {
        try {
            List<BookDTO> books = bookService.findBooksByCategory(categoryName);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de livres par catégorie", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de la recherche de livres : " + e.getMessage()));
        }
    }

    @Operation(summary = "Get authors by book")
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

    @Operation(summary = "Add author to book")
    @PostMapping("/{bookId}/authors/{authorId}")
    public ResponseEntity<?> addAuthorToBook(@Parameter(description = "Book ID") @PathVariable Long bookId,
            @Parameter(description = "Author ID") @PathVariable Long authorId) {
        try {
            Set<Author> authors = bookService.addAuthorToBook(bookId, authorId);
            return ResponseEntity.ok(authors);
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout de l'auteur " + authorId + " au livre " + bookId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de l'ajout de l'auteur : " + e.getMessage()));
        }
    }

    @Operation(summary = "Remove author from book")
    @DeleteMapping("/{bookId}/authors/{authorId}")
    public ResponseEntity<?> removeAuthorFromBook(@Parameter(description = "Book ID") @PathVariable Long bookId,
            @Parameter(description = "Author ID") @PathVariable Long authorId) {
        try {
            Set<Author> authors = bookService.removeAuthorFromBook(bookId, authorId);
            return ResponseEntity.ok(authors);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'auteur " + authorId + " du livre " + bookId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de la suppression de l'auteur : " + e.getMessage()));
        }
    }

    @Operation(summary = "Get categories by book")
    @GetMapping("/{id}/categories")
    public ResponseEntity<?> getCategoriesByBook(@Parameter(description = "Book ID") @PathVariable Long id) {
        try {
            Set<CategoryDTO> categories = bookService.getCategoriesByBook(id);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des catégories pour le livre " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de la récupération des catégories : " + e.getMessage()));
        }
    }

    @Operation(summary = "Add category to book")
    @PostMapping("/{bookId}/categories/{categoryId}")
    public ResponseEntity<?> addCategoryToBook(@Parameter(description = "Book ID") @PathVariable Long bookId,
            @Parameter(description = "Category ID") @PathVariable Long categoryId) {
        try {
            Set<Category> categories = bookService.addCategoryToBook(bookId, categoryId);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout de la catégorie " + categoryId + " au livre " + bookId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de l'ajout de la catégorie : " + e.getMessage()));
        }
    }

    @Operation(summary = "Remove category from book")
    @DeleteMapping("/{bookId}/categories/{categoryId}")
    public ResponseEntity<?> removeCategoryFromBook(@Parameter(description = "Book ID") @PathVariable Long bookId,
            @Parameter(description = "Category ID") @PathVariable Long categoryId) {
        try {
            Set<Category> categories = bookService.removeCategoryFromBook(bookId, categoryId);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la catégorie " + categoryId + " du livre " + bookId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de la suppression de la catégorie : " + e.getMessage()));
        }
    }

    // Classe interne pour les réponses d'erreur
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