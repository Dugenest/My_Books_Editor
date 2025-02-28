package com.afci.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.afci.data.Category;
import com.afci.service.CategoryService;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @SuppressWarnings("null")
	@Test
    void testGetAllCategories() {
        List<Category> categories = Arrays.asList(
            new Category("Science Fiction", "Books related to sci-fi stories."),
            new Category("Fantasy", "Books related to fantasy worlds."));
        when(categoryService.getAllCategories()).thenReturn(categories);

        ResponseEntity<List<Category>> response = categoryController.getAllCategories();
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @SuppressWarnings("null")
	@Test
    void testGetCategoryById() {
        Category category = new Category("Science Fiction", "Books related to sci-fi stories.");
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(category));

        ResponseEntity<Optional<Category>> response = categoryController.getCategoryById(1L);
        assertTrue(response.getBody().isPresent());
        assertEquals("Science Fiction", response.getBody().get().getName());
    }

    @SuppressWarnings("null")
	@Test
    void testCreateCategory() {
        Category category = new Category("Science Fiction", "Books related to sci-fi stories.");
        when(categoryService.createCategory(any())).thenReturn(category);

        ResponseEntity<Category> response = categoryController.createCategory(category);
        assertNotNull(response.getBody());
        assertEquals("Science Fiction", response.getBody().getName());
    }

    @SuppressWarnings("null")
	@Test
    void testUpdateCategory() {
        Category category = new Category("Science Fiction", "Books related to sci-fi stories.");
        when(categoryService.updateCategory(any())).thenReturn(category);

        ResponseEntity<Category> response = categoryController.updateCategory(1L, category);
        assertEquals("Science Fiction", response.getBody().getName());
    }

    @SuppressWarnings("deprecation")
	@Test
    void testDeleteCategory() {
        doNothing().when(categoryService).deleteCategory(1L);

        ResponseEntity<Void> response = categoryController.deleteCategory(1L);
        assertEquals(204, response.getStatusCodeValue());
    }
}
