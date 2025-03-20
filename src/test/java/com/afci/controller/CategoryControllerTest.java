package com.afci.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = Arrays.asList(
                new Category("Science Fiction", "Books related to sci-fi stories."),
                new Category("Fantasy", "Books related to fantasy worlds."));
                
        Page<Category> categoryPage = mock(Page.class);
        when(categoryPage.getContent()).thenReturn(categories);
        when(categoryService.getAllCategories(pageable)).thenReturn(categoryPage);

        // When
        ResponseEntity<?> response = categoryController.getAllCategories(pageable);
        
        // Then
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @SuppressWarnings("null")
    @Test
    void testGetCategoryById() {
        Category category = new Category("Science Fiction", "Books related to sci-fi stories.");
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(category));

        ResponseEntity<?> response = categoryController.getCategoryById(1L);
        assertTrue(response.getStatusCode() == HttpStatus.OK);
        Category responseCategory = (Category) response.getBody();
        assertEquals("Science Fiction", responseCategory.getName());
    }

    @SuppressWarnings("null")
    @Test
    void testCreateCategory() {
        Category category = new Category("Science Fiction", "Books related to sci-fi stories.");
        when(categoryService.createCategory(any())).thenReturn(category);

        ResponseEntity<?> response = categoryController.createCategory(category);
        assertNotNull(response.getBody());
        Category createdCategory = (Category) response.getBody();
        assertEquals("Science Fiction", createdCategory.getName());
    }

    @SuppressWarnings("null")
    @Test
    void testUpdateCategory() {
        Category category = new Category("Science Fiction", "Books related to sci-fi stories.");
        when(categoryService.updateCategory(any())).thenReturn(category);

        ResponseEntity<?> response = categoryController.updateCategory(1L, category);
        assertTrue(response.getStatusCode() == HttpStatus.OK);
        Category updatedCategory = (Category) response.getBody();
        assertEquals("Science Fiction", updatedCategory.getName());
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryService).deleteCategory(1L);

        ResponseEntity<?> response = categoryController.deleteCategory(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getAllCategories_ShouldReturnCategories() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Category category = new Category("Science Fiction", "Books related to sci-fi stories.");
        when(categoryService.getAllCategories(pageable)).thenReturn(Page.empty());
        
        // When
        ResponseEntity<?> response = categoryController.getAllCategories(pageable);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
