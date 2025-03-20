package com.afci.service;

import com.afci.data.Book;
import com.afci.data.Editor;
import com.afci.repository.EditorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EditorService {

    @Autowired
    private EditorRepository editorRepository;

    public List<Editor> getAllEditors() {
        return editorRepository.findAll();
    }
    
    public Page<Editor> getAllEditors(Pageable pageable) {
        return editorRepository.findAll(pageable);
    }

    public Optional<Editor> getEditorById(Long id) {
        return editorRepository.findById(id);
    }

    public Editor createEditor(Editor editor) {
        return editorRepository.save(editor);
    }

    public Editor updateEditor(Editor editor) {
        // Utiliser getId() au lieu de getUserId()
        if (editorRepository.existsById(editor.getId())) {
            return editorRepository.save(editor);
        }
        throw new RuntimeException("Éditeur non trouvé avec l'ID : " + editor.getId());
    }

    public void deleteEditor(Long id) {
        editorRepository.deleteById(id);
    }

    // Nouvelle méthode pour remplacer findByCompanyName
    public List<Editor> findByCompanyName() {
        return editorRepository.findAll().stream()
            .filter(editor -> editor.getCompany() != null)
            .collect(Collectors.toList());
    }

    public Set<Book> getBooksByEditor(Long id) {
        Optional<Editor> editor = editorRepository.findById(id);
        return editor.map(Editor::getBooks).orElse(Collections.emptySet());
    }

    // Nouvelle méthode pour rechercher par company
    public List<Editor> findByCompany(String company) {
        return editorRepository.findAll().stream()
            .filter(editor -> editor.getCompany() != null && editor.getCompany().equals(company))
            .collect(Collectors.toList());
    }
}