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

import com.afci.data.Category;
import com.afci.exception.CategoryNotFoundException;
import com.afci.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/categories")
@Tag(name = "Category Management", description = "APIs for managing categories")
public class CategoryController {

	private final CategoryService cs;

	public CategoryController(CategoryService cs) {
		this.cs = cs;
	}

	@GetMapping
	@Operation(summary = "List all categories", description = "Fetches all categories available in the system.")
	@ApiResponse(responseCode = "200", description = "Successfully retrieved list of categories")
	public ResponseEntity<List<Category>> getAllCategories() {
		List<Category> categories = cs.getAllCategorys();
		if (categories.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get a category by ID", description = "Fetches a single category using its ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Category found"),
		@ApiResponse(responseCode = "404", description = "Category not found")
	})
	public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
		try {
			Category category = cs.getCategoryById(id);
			return ResponseEntity.ok(category);
		} catch (CategoryNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@PostMapping
	@Operation(summary = "Add a new category", description = "Creates a new category.")
	@ApiResponse(responseCode = "201", description = "Category successfully created")
	public ResponseEntity<Category> addCategory(@RequestBody Category c) {
		Category createdCategory = cs.addCategory(c);
		return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update a category", description = "Updates an existing category.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Category updated successfully"),
		@ApiResponse(responseCode = "404", description = "Category not found")
	})
	public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
		try {
			Category updatedCategory = cs.updateCategory(id, category);
			return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
		} catch (CategoryNotFoundException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a category", description = "Removes a category by its ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Category deleted successfully"),
		@ApiResponse(responseCode = "404", description = "Category not found")
	})
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
		try {
			cs.deleteCategory(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (CategoryNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
