package com.afci.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.afci.data.Category;
import com.afci.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Management", description = "Category operations")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Get all categories")
    @GetMapping
    public ResponseEntity<?> getAllCategories(Pageable pageable) {
        try {
            Page<Category> categories = categoryService.getAllCategories(pageable);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Erreur lors de la récupération des catégories", e);
            // Retourner une page vide au lieu d'une erreur 500
            return ResponseEntity.ok(Page.empty());
        }
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(
        @Parameter(description = "Category ID") @PathVariable Long id
    ) {
        try {
            Optional<Category> category = categoryService.getCategoryById(id);
            if (category.isPresent()) {
                return ResponseEntity.ok(category.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Catégorie non trouvée avec l'ID : " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erreur lors de la récupération de la catégorie : " + e.getMessage()));
        }
    }

    @Operation(summary = "Get books by category")
    @GetMapping("/{id}/books")
    public ResponseEntity<?> getBooksByCategory(
        @Parameter(description = "Category ID") @PathVariable Long id
    ) {
        try {
            return ResponseEntity.ok(categoryService.getBooksByCategory(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erreur lors de la récupération des livres : " + e.getMessage()));
        }
    }

    @Operation(summary = "Create category")
    @PostMapping
    public ResponseEntity<?> createCategory(
        @Parameter(description = "Category details") 
        @Valid @RequestBody Category category
    ) {
        try {
            Category createdCategory = categoryService.createCategory(category);
            return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erreur lors de la création de la catégorie : " + e.getMessage()));
        }
    }

    @Operation(summary = "Update category")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
        @Parameter(description = "Category ID") @PathVariable Long id,
        @Parameter(description = "Category details") 
        @Valid @RequestBody Category category
    ) {
        try {
            category.setId(id);
            Category updatedCategory = categoryService.updateCategory(category);
            return ResponseEntity.ok(updatedCategory);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erreur lors de la mise à jour de la catégorie : " + e.getMessage()));
        }
    }

    @Operation(summary = "Delete category")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(
        @Parameter(description = "Category ID") @PathVariable Long id
    ) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erreur lors de la suppression de la catégorie : " + e.getMessage()));
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