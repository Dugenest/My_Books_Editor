package com.afci.service;

import com.afci.data.Editor;
import com.afci.data.EditorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EditorService {

    @Autowired
    private EditorRepository editorRepository;

    public List<Editor> getAllEditors() {
        return editorRepository.findAll();
    }

    public Optional<Editor> getEditorById(Long id) {
        return editorRepository.findById(id);
    }

    public Editor createEditor(Editor editor) {
        return editorRepository.save(editor);
    }

    public Editor updateEditor(Editor editor) {
        if (editorRepository.existsById(editor.getId())) {
            return editorRepository.save(editor);
        }
        throw new RuntimeException("Éditeur non trouvé avec l'ID : " + editor.getId());
    }

    public void deleteEditor(Long id) {
        editorRepository.deleteById(id);
    }

    public List<Editor> findByCompanyName(String companyName) {
        return editorRepository.findByCompanyNameContainingIgnoreCase(companyName);
    }

	public Object getBooksByEditor(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}