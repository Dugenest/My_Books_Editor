package com.afci.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.afci.data.Category;
import com.afci.data.CategoryRepository;
import com.afci.exception.NotFoundException;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Obtenir toutes les catégories
    public List<Category> getAllCategorys() {
        return (List<Category>) categoryRepository.findAll();
    }

    // Obtenir une catégorie par ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found with id: " + id));
    }

    // Ajouter une catégorie
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Mettre à jour une catégorie existante
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = getCategoryById(id); // Lève une exception si non trouvé
        category.setNameCategory(categoryDetails.getNameCategory());
        return categoryRepository.save(category);
    }

    // Supprimer une catégorie
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id); // Lève une exception si non trouvé
        categoryRepository.delete(category);
    }
}
