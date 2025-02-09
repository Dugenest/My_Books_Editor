package com.afci.service;

import com.afci.data.Category;
import com.afci.data.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category) {
        if (categoryRepository.existsById(category.getId())) {
            return categoryRepository.save(category);
        }
        throw new RuntimeException("Catégorie non trouvée avec l'ID : " + category.getId());
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category findByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name);
    }

	public Object getBooksByCategory1(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getBooksByCategory(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}