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
        editor.setCompanyId(1L);
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
        assertEquals(editor.getCompanyId(), result.get().getCompanyId());
    }

    @Test
    void createEditor_ShouldReturnSavedEditor() {
        when(editorRepository.save(any(Editor.class))).thenReturn(editor);

        Editor result = editorService.createEditor(editor);

        assertNotNull(result);
        assertEquals(editor.getCompanyId(), result.getCompanyId());
    }

    @Test
    void updateEditor_WhenExists_ShouldReturnUpdatedEditor() {
        when(editorRepository.existsById(1L)).thenReturn(true);
        when(editorRepository.save(any(Editor.class))).thenReturn(editor);

        Editor result = editorService.updateEditor(editor);

        assertNotNull(result);
        assertEquals(editor.getCompanyId(), result.getCompanyId());
    }

    @Test
    void updateEditor_WhenNotExists_ShouldThrowException() {
        when(editorRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> editorService.updateEditor(editor));
    }

    @Test
    void deleteEditor_ShouldCallRepositoryDelete() {
        doNothing().when(editorRepository).deleteById(1L);

        editorService.deleteEditor(1L);

        verify(editorRepository).deleteById(1L);
    }

    @Test
    void findByCompanyName_ShouldReturnEditors() {
        List<Editor> editors = Arrays.asList(editor);
        when(editorRepository.findAll()).thenReturn(editors);

        List<Editor> result = editorService.findByCompanyName("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getCompanyId());
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
}