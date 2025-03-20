package com.afci.service;

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

import com.afci.data.Book;
import com.afci.data.Editor;
import com.afci.repository.EditorRepository;

@ExtendWith(MockitoExtension.class)
public class EditorServiceTest {

    @Mock
    private EditorRepository editorRepository;

    @InjectMocks
    private EditorService editorService;

    private Editor editor;

    @BeforeEach
    void setUp() {
        editor = new Editor();
        editor.setId(1L);
        editor.setCompany("Test Company");
    }

    @Test
    void getAllEditors_ShouldReturnAllEditors() {
        List<Editor> editors = Arrays.asList(editor);
        when(editorRepository.findAll()).thenReturn(editors);

        List<Editor> result = editorService.getAllEditors();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(editorRepository).findAll();
    }

    @Test
    void getEditorById_ShouldReturnEditor() {
        when(editorRepository.findById(1L)).thenReturn(Optional.of(editor));

        Optional<Editor> result = editorService.getEditorById(1L);

        assertTrue(result.isPresent());
        assertEquals(editor.getCompany(), result.get().getCompany());
    }

    @Test
    public void testCreateEditor() {
        // Given
        Editor editor = new Editor();
        editor.setCompany("Test Company");
        
        when(editorRepository.save(any(Editor.class))).thenReturn(editor);
        
        // When
        Editor result = editorService.createEditor(editor);
        
        // Then
        assertNotNull(result);
        assertEquals(editor.getCompany(), result.getCompany());
        verify(editorRepository, times(1)).save(editor);
    }

    @Test
    public void testUpdateEditor() {
        // Given
        Editor editor = new Editor();
        editor.setId(1L);
        editor.setCompany("Test Company");
        
        when(editorRepository.existsById(anyLong())).thenReturn(true);
        when(editorRepository.save(any(Editor.class))).thenReturn(editor);
        
        // When
        Editor result = editorService.updateEditor(editor);
        
        // Then
        assertNotNull(result);
        assertEquals(editor.getCompany(), result.getCompany());
        verify(editorRepository, times(1)).save(editor);
    }

    @Test
    void deleteEditor_ShouldCallRepositoryDelete() {
        doNothing().when(editorRepository).deleteById(1L);

        editorService.deleteEditor(1L);

        verify(editorRepository).deleteById(1L);
    }

    @Test
    public void testFindByCompanyName() {
        // Given
        List<Editor> editors = Arrays.asList(
            new Editor(), new Editor()
        );
        when(editorRepository.findAll()).thenReturn(editors);
        
        // When
        List<Editor> result = editorService.findByCompanyName();
        
        // Then
        assertNotNull(result);
        verify(editorRepository, times(1)).findAll();
    }

    @Test
    void getBooksByEditor_ShouldReturnBooks() {
        Set<Book> mockBooks = Collections.singleton(new Book());
        editor.setBooks(mockBooks);
        when(editorRepository.findById(1L)).thenReturn(Optional.of(editor));

        Set<Book> result = editorService.getBooksByEditor(1L);

        assertNotNull(result);
        assertEquals(mockBooks, result);
    }
    
    @Test
    public void testFindByCompany() {
        // Given
        Editor editor1 = new Editor();
        editor1.setCompany("Test Company");
        
        Editor editor2 = new Editor();
        editor2.setCompany("Another Company");
        
        List<Editor> editors = Arrays.asList(editor1, editor2);
        when(editorRepository.findAll()).thenReturn(editors);
        
        // When
        List<Editor> result = editorService.findByCompany("Test Company");
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Company", result.get(0).getCompany());
        verify(editorRepository, times(1)).findAll();
    }
}