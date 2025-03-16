package com.afci.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afci.data.Category;
import com.afci.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Management", description = "Category operations")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Get all categories")
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Category>> getCategoryById(
        @Parameter(description = "Category ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(summary = "Get books by category")
    @GetMapping("/{id}/books")
    public ResponseEntity<Object> getBooksByCategory(
        @Parameter(description = "Category ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok(categoryService.getBooksByCategory(id));
    }

    @Operation(summary = "Create category")
    @PostMapping
    public ResponseEntity<Category> createCategory(
        @Parameter(description = "Category details") 
        @Valid @RequestBody Category category
    ) {
        return new ResponseEntity<>(categoryService.createCategory(category), 
            HttpStatus.CREATED);
    }

    @Operation(summary = "Update category")
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
        @Parameter(description = "Category ID") @PathVariable Long id,
        @Parameter(description = "Category details") 
        @Valid @RequestBody Category category
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(category));
    }

    @Operation(summary = "Delete category")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
        @Parameter(description = "Category ID") @PathVariable Long id
    ) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}