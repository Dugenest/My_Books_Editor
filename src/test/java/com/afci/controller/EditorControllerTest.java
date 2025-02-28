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
        editor.setCompanyName("Test Publisher");
        editor.setAddress("123 Publishing Street");
    }

    @Test
    void getAllEditors_ShouldReturnEditors() {
        List<Editor> editors = Arrays.asList(editor);
        when(editorService.getAllEditors()).thenReturn(editors);

        ResponseEntity<List<Editor>> response = editorController.getAllEditors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getEditorById_ShouldReturnEditor() {
        when(editorService.getEditorById(1L)).thenReturn(Optional.of(editor));

        ResponseEntity<Optional<Editor>> response = editorController.getEditorById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
    }

    @Test
    void createEditor_ShouldReturnCreatedEditor() {
        when(editorService.createEditor(any(Editor.class))).thenReturn(editor);

        ResponseEntity<Editor> response = editorController.createEditor(editor);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(editor.getCompanyName(), response.getBody().getCompanyName());
    }

    @Test
    void updateEditor_ShouldReturnUpdatedEditor() {
        when(editorService.updateEditor(any(Editor.class))).thenReturn(editor);

        ResponseEntity<Editor> response = editorController.updateEditor(1L, editor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(editor.getCompanyName(), response.getBody().getCompanyName());
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
} 