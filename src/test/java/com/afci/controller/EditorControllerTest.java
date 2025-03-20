package com.afci.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.afci.data.Editor;
import com.afci.data.Book;
import com.afci.service.EditorService;

@ExtendWith(MockitoExtension.class)
public class EditorControllerTest {

    @Mock
    private EditorService editorService;

    @InjectMocks
    private EditorController editorController;

    private Editor editor;

    @BeforeEach
    void setUp() {
        editor = new Editor();
        editor.setId(1L);
        editor.setCompany("Test Company");
    }

    @SuppressWarnings("null")
    @Test
    void getAllEditors_ShouldReturnEditors() {
        // Given
        List<Editor> editors = Arrays.asList(editor);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Editor> editorPage = new PageImpl<>(editors, pageable, editors.size());
        when(editorService.getAllEditors(pageable)).thenReturn(editorPage);

        // When
        ResponseEntity<?> response = editorController.getAllEditors(pageable);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(editorPage, response.getBody());
    }

    @SuppressWarnings("null")
    @Test
    void getEditorById_ShouldReturnEditor() {
        when(editorService.getEditorById(1L)).thenReturn(Optional.of(editor));

        ResponseEntity<Optional<Editor>> response = editorController.getEditorById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
        assertEquals(editor.getCompany(), response.getBody().get().getCompany());
    }

    @SuppressWarnings("null")
    @Test
    void createEditor_ShouldReturnCreatedEditor() {
        when(editorService.createEditor(any(Editor.class))).thenReturn(editor);

        ResponseEntity<Editor> response = editorController.createEditor(editor);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(editor.getCompany(), response.getBody().getCompany());
    }

    @SuppressWarnings("null")
    @Test
    void updateEditor_ShouldReturnUpdatedEditor() {
        when(editorService.updateEditor(any(Editor.class))).thenReturn(editor);

        ResponseEntity<Editor> response = editorController.updateEditor(1L, editor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(editor.getCompany(), response.getBody().getCompany());
    }

    @Test
    void deleteEditor_ShouldReturnNoContent() {
        doNothing().when(editorService).deleteEditor(1L);

        ResponseEntity<Void> response = editorController.deleteEditor(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getBooksByEditor_ShouldReturnBooks() {
        Set<Book> mockBooks = Collections.singleton(new Book());
        when(editorService.getBooksByEditor(1L)).thenReturn(mockBooks);

        ResponseEntity<Set<Book>> response = editorController.getBooksByEditor(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetAllEditors() {
        // Given
        List<Editor> editors = Arrays.asList(new Editor(), new Editor());
        Pageable pageable = PageRequest.of(0, 10);
        Page<Editor> editorPage = new PageImpl<>(editors, pageable, editors.size());
        
        // When
        when(editorService.getAllEditors(pageable)).thenReturn(editorPage);
        ResponseEntity<?> response = editorController.getAllEditors(pageable);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(editorPage, response.getBody());
        verify(editorService, times(1)).getAllEditors(pageable);
    }

    @Test
    public void testUpdateEditor() {
        // Given
        long editorId = 1L;
        Editor editor = new Editor();
        editor.setId(editorId);
        editor.setCompany("Test Company");
        
        // When
        when(editorService.updateEditor(editor)).thenReturn(editor);
        ResponseEntity<Editor> response = editorController.updateEditor(editorId, editor);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(editor.getCompany(), response.getBody().getCompany());
    }
}