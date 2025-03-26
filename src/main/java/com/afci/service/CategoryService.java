package com.afci.service;

import com.afci.data.Category;
import com.afci.data.Book;
import com.afci.repository.CategoryRepository;
import com.afci.exception.AlreadyExistsException;
import com.afci.exception.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.logging.Logger;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;

@Service
@Transactional
public class CategoryService {

    private static final Logger logger = Logger.getLogger(CategoryService.class.getName());

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();
            // Ne pas initialiser manuellement les collections pour éviter les erreurs de proxy
            return categories;
        } catch (Exception e) {
            logger.severe("Erreur lors de la récupération des catégories sans pagination: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Page<Category> getAllCategories(Pageable pageable) {
        try {
            logger.info("Récupération de toutes les catégories avec pagination: " + pageable);
            Page<Category> categories = categoryRepository.findAll(pageable);
            
            // Ne pas initialiser les collections pour éviter les erreurs de proxy
            
            return categories;
        } catch (Exception e) {
            logger.severe("Erreur lors de la récupération des catégories avec pagination: " + e.getMessage());
            e.printStackTrace();
            return Page.empty(pageable);
        }
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        logger.info("Tentative de création de catégorie : " + category.getName());

        if (existsByName(category.getName())) {
            logger.warning("Tentative de création d'une catégorie existante : " + category.getName());
            throw new AlreadyExistsException(
                    "Une catégorie avec le nom '" + category.getName() + "' existe déjà");
        }

        Category savedCategory = categoryRepository.save(category);
        logger.info("Catégorie créée avec succès : " + savedCategory.getName());
        return savedCategory;
    }

    public Category updateCategory(Category category) {
        if (category.getId() == null) {
            throw new IllegalArgumentException("L'ID de la catégorie ne peut pas être null");
        }

        Optional<Category> existingCategoryOpt = categoryRepository.findById(category.getId());
        if (!existingCategoryOpt.isPresent()) {
            throw new NotFoundException("Catégorie non trouvée avec l'ID : " + category.getId());
        }

        Category existingCategory = existingCategoryOpt.get();

        // Vérifier si le nouveau nom est déjà utilisé par une autre catégorie
        Category categoryWithSameName = categoryRepository.findByNameIgnoreCase(category.getName());
        if (categoryWithSameName != null && !categoryWithSameName.getId().equals(category.getId())) {
            throw new AlreadyExistsException(
                    "Une catégorie avec le nom '" + category.getName() + "' existe déjà");
        }

        // Préserver la collection des livres
        Set<Book> books = existingCategory.getBooks();
        category.setBooks(books);

        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (!categoryOpt.isPresent()) {
            throw new NotFoundException("Catégorie non trouvée avec l'ID : " + id);
        }

        Category category = categoryOpt.get();
        if (category.getBooks() != null && !category.getBooks().isEmpty()) {
            throw new IllegalStateException("Impossible de supprimer la catégorie car elle est associée à " +
                    category.getBooks().size()
                    + " livre(s). Veuillez d'abord retirer tous les livres de cette catégorie.");
        }

        categoryRepository.deleteById(id);
    }

    public Category findByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name);
    }

    public Set<Book> getBooksByCategory(Long id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (!categoryOpt.isPresent()) {
            throw new NotFoundException("Catégorie non trouvée avec l'ID : " + id);
        }
        return categoryOpt.get().getBooks();
    }

    // Méthodes supplémentaires
    public boolean existsByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name) != null;
    }

    public List<Category> searchCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }
}