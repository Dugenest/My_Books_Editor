package com.afci.service;

import com.afci.data.Book;
import com.afci.data.Category;
import com.afci.data.Author;
import com.afci.data.Editor;
import com.afci.dto.AuthorDTO;
import com.afci.dto.BookDTO;
import com.afci.dto.CategoryDTO;
import com.afci.dto.EditorDTO;
import com.afci.repository.BookRepository;
import com.afci.repository.CategoryRepository;
import com.afci.repository.AuthorRepository;
import com.afci.repository.EditorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private EditorRepository editorRepository;

    @Transactional(readOnly = true)
    public Page<BookDTO> getAllBooks(Pageable pageable) {
        logger.debug("Récupération de tous les livres avec pagination: {}", pageable);
        Page<Book> books = bookRepository.findAll(pageable);

        List<BookDTO> bookDTOs = books.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(bookDTOs, pageable, books.getTotalElements());
    }

    // Méthode de conversion de Book en BookDTO
    private BookDTO convertToDTO(Book book) {
        logger.debug("Conversion du livre en DTO: {}", book.getTitle());
        BookDTO dto = new BookDTO();

        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getISBN());
        dto.setDetail(book.getDetail());
        dto.setPrice(book.getPrice());
        dto.setStock(book.getStock());
        dto.setPicture(book.getPicture());
        dto.setPublishDate(book.getPublishDate());
        dto.setRewardDate(book.getRewardDate());

        // Conversion de l'éditeur
        if (book.getEditor() != null) {
            logger.debug("Conversion de l'éditeur pour le livre: {}", book.getTitle());
            EditorDTO editorDTO = new EditorDTO();
            editorDTO.setId(book.getEditor().getId());
            editorDTO.setCompany(book.getEditor().getCompany());
            dto.setEditor(editorDTO);
        }

        // Conversion des catégories
        if (book.getCategories() != null) {
            logger.debug("Conversion des catégories pour le livre: {}", book.getTitle());
            Set<CategoryDTO> categoryDTOs = book.getCategories().stream()
                    .map(this::convertCategoryToDTO)
                    .collect(Collectors.toSet());
            dto.setCategories(categoryDTOs);
        }

        // Conversion des auteurs
        if (book.getAuthors() != null) {
            logger.debug("Conversion des auteurs pour le livre: {}", book.getTitle());
            Set<AuthorDTO> authorDTOs = book.getAuthors().stream()
                    .map(this::convertAuthorToDTO)
                    .collect(Collectors.toSet());
            dto.setAuthors(authorDTOs);
        }

        return dto;
    }

    // Méthode de conversion de Category en CategoryDTO
    private CategoryDTO convertCategoryToDTO(Category category) {
        logger.debug("Conversion de la catégorie en DTO: {}", category.getName());
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setColor(category.getColor());
        dto.setIcon(category.getIcon());
        return dto;
    }

    // Méthode de conversion de Author en AuthorDTO
    private AuthorDTO convertAuthorToDTO(Author author) {
        logger.debug("Conversion de l'auteur en DTO: {} {}", author.getFirstName(), author.getLastName());
        AuthorDTO dto = new AuthorDTO();
        dto.setId(author.getId());
        dto.setFirstName(author.getFirstName());
        dto.setLastName(author.getLastName());
        dto.setNationality(author.getAuthorNationality());
        dto.setBiography(author.getBiography());
        dto.setBirthDate(author.getBirthDate());
        return dto;
    }

    /**
     * Recherche un livre par son identifiant.
     * 
     * @param id Identifiant du livre.
     * @return Un objet Optional contenant le livre s'il est trouvé.
     */
    public Optional<BookDTO> getBookById(Long id) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        return bookOpt.map(this::convertToDTO);
    }

    /**
     * Ajoute un nouveau livre à la base de données avec gestion des relations.
     * 
     * @param book Le livre à enregistrer.
     * @return Le livre enregistré.
     */
    public Book createBook(Book book) {
        // Gérer les catégories
        if (book.getCategories() != null && !book.getCategories().isEmpty()) {
            Set<Category> categories = new HashSet<>();
            for (Category category : book.getCategories()) {
                if (category.getId() != null) {
                    Category existingCategory = categoryRepository.findById(category.getId())
                            .orElseThrow(() -> new RuntimeException("Catégorie non trouvée: " + category.getId()));
                    categories.add(existingCategory);
                }
            }
            book.setCategories(categories);
        }

        // Note: J'ai commenté ces lignes car il semble que book.getAuthor() et
        // book.setAuthor()
        // ne sont peut-être pas les bonnes méthodes (vous utilisez getAuthors() et
        // setAuthors() ailleurs)
        // Si votre entité Book a un champ author et une collection authors, ajustez
        // selon votre modèle
        /*
         * // Gérer l'auteur
         * if (book.getAuthor() != null && book.getAuthor().getId() != null) {
         * Author author = authorRepository.findById(book.getAuthor().getId())
         * .orElseThrow(() -> new RuntimeException("Auteur non trouvé: " +
         * book.getAuthor().getId()));
         * book.setAuthor(author);
         * }
         */

        // Gérer l'éditeur
        if (book.getEditor() != null && book.getEditor().getId() != null) {
            Editor editor = editorRepository.findById(book.getEditor().getId())
                    .orElseThrow(() -> new RuntimeException("Éditeur non trouvé: " + book.getEditor().getId()));
            book.setEditor(editor);
        }

        return bookRepository.save(book);
    }

    /**
     * Met à jour un livre existant dans la base de données avec gestion des
     * relations.
     * 
     * @param book Le livre avec les nouvelles informations.
     * @return Le livre mis à jour.
     * @throws RuntimeException Si le livre avec l'ID donné n'existe pas.
     */
    public Book updateBook(Book book) {
        if (!bookRepository.existsById(book.getId())) {
            throw new RuntimeException("Livre non trouvé avec l'ID : " + book.getId());
        }

        // Récupérer le livre existant
        Book existingBook = bookRepository.findById(book.getId())
                .orElseThrow(() -> new RuntimeException("Livre non trouvé avec l'ID : " + book.getId()));

        // Préserver les relations existantes si non spécifiées dans la mise à jour
        if (book.getCategories() == null || book.getCategories().isEmpty()) {
            book.setCategories(existingBook.getCategories());
        } else {
            // Mettre à jour les catégories
            Set<Category> categories = new HashSet<>();
            for (Category category : book.getCategories()) {
                if (category.getId() != null) {
                    Category existingCategory = categoryRepository.findById(category.getId())
                            .orElseThrow(() -> new RuntimeException("Catégorie non trouvée: " + category.getId()));
                    categories.add(existingCategory);
                }
            }
            book.setCategories(categories);
        }

        // Note: Commenté pour la même raison que dans createBook
        /*
         * // Gérer l'auteur
         * if (book.getAuthor() == null || book.getAuthor().getId() == null) {
         * book.setAuthor(existingBook.getAuthor());
         * } else {
         * Author author = authorRepository.findById(book.getAuthor().getId())
         * .orElseThrow(() -> new RuntimeException("Auteur non trouvé: " +
         * book.getAuthor().getId()));
         * book.setAuthor(author);
         * }
         */

        // Gérer les auteurs
        if (book.getAuthors() == null || book.getAuthors().isEmpty()) {
            book.setAuthors(existingBook.getAuthors());
        }

        // Gérer l'éditeur
        if (book.getEditor() == null || book.getEditor().getId() == null) {
            book.setEditor(existingBook.getEditor());
        } else {
            Editor editor = editorRepository.findById(book.getEditor().getId())
                    .orElseThrow(() -> new RuntimeException("Éditeur non trouvé: " + book.getEditor().getId()));
            book.setEditor(editor);
        }

        // Conserver l'administrateur s'il n'est pas spécifié
        if (book.getAdministrator() == null) {
            book.setAdministrator(existingBook.getAdministrator());
        }

        // Conserver l'utilisateur s'il n'est pas spécifié
        if (book.getUser() == null) {
            book.setUser(existingBook.getUser());
        }

        // Préserver les collections de relations qui ne devraient pas être modifiées
        // directement
        book.setOrders(existingBook.getOrders());
        book.setBasketBooks(existingBook.getBasketBooks());

        return bookRepository.save(book);
    }

    /**
     * Supprime un livre par son identifiant après avoir vérifié les dépendances.
     * 
     * @param id Identifiant du livre à supprimer.
     * @throws RuntimeException Si le livre a des commandes ou des paniers associés.
     */
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé avec l'ID : " + id));

        // Vérifier si le livre a des commandes
        if (book.getOrders() != null && !book.getOrders().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer ce livre car il est associé à " +
                    book.getOrders().size() + " commande(s).");
        }

        // Vérifier si le livre est dans des paniers
        if (book.getBasketBooks() != null && !book.getBasketBooks().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer ce livre car il est présent dans " +
                    book.getBasketBooks().size() + " panier(s).");
        }

        // Supprimer les associations avec les catégories
        book.setCategories(new HashSet<>());
        bookRepository.save(book);

        // Maintenant supprimer le livre
        bookRepository.deleteById(id);
    }

    /**
     * Recherche des livres dont le nom ou le prénom de l'auteur contient un texte
     * donné (insensible à la casse).
     * 
     * @param lastName  Nom de famille de l'auteur.
     * @param firstName Prénom de l'auteur.
     * @return Liste des livres correspondant à la recherche.
     */
    public List<BookDTO> findByAuthorName(String lastName, String firstName) {
        List<Book> books = bookRepository
                .findByAuthor_LastNameContainingIgnoreCaseOrAuthor_FirstNameContainingIgnoreCase(lastName, firstName);
        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Recherche des livres dont le titre contient un texte donné (insensible à la
     * casse).
     * 
     * @param title Partie du titre à rechercher.
     * @return Liste des livres correspondant à la recherche.
     */
    public List<BookDTO> findBooksByTitle(String title) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Recherche des livres appartenant à une catégorie spécifique.
     * 
     * @param categoryName Nom de la catégorie.
     * @return Liste des livres de cette catégorie.
     */
    public List<BookDTO> findBooksByCategory(String categoryName) {
        List<Book> books = bookRepository.findByCategory_Name(categoryName);
        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Récupère les dernières parutions de livres
     * 
     * @param limit Le nombre de livres à récupérer
     * @return Une liste des derniers livres publiés
     */
    public List<BookDTO> getNewReleases(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("publishDate").descending());
        List<Book> books = bookRepository.findAll(pageable).getContent();
        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Récupère les livres les plus populaires
     * 
     * @param limit Le nombre de livres à récupérer
     * @return Une liste des livres les plus populaires
     */
    public List<BookDTO> getPopularBooks(int limit) {
        // Pour l'instant, on retourne simplement les premiers livres
        Pageable pageable = PageRequest.of(0, limit);
        List<Book> books = bookRepository.findAll(pageable).getContent();
        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Méthodes pour gérer les auteurs
    /**
     * Récupère les auteurs d'un livre
     * 
     * @param bookId ID du livre
     * @return Ensemble des auteurs du livre
     */
    public Set<AuthorDTO> getAuthorsByBook(Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            return book.get().getAuthors().stream()
                    .map(this::convertAuthorToDTO)
                    .collect(Collectors.toSet());
        }
        throw new RuntimeException("Livre non trouvé avec l'ID : " + bookId);
    }

    /**
     * Ajoute un auteur à un livre
     * 
     * @param bookId   ID du livre
     * @param authorId ID de l'auteur
     * @return Ensemble des auteurs du livre après l'ajout
     */
    public Set<Author> addAuthorToBook(Long bookId, Long authorId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (!bookOpt.isPresent()) {
            throw new RuntimeException("Livre non trouvé avec l'ID : " + bookId);
        }

        Optional<Author> authorOpt = authorRepository.findById(authorId);
        if (!authorOpt.isPresent()) {
            throw new RuntimeException("Auteur non trouvé avec l'ID : " + authorId);
        }

        Book book = bookOpt.get();
        Author author = authorOpt.get();

        book.addAuthor(author);
        bookRepository.save(book);

        return book.getAuthors();
    }

    /**
     * Retire un auteur d'un livre
     * 
     * @param bookId   ID du livre
     * @param authorId ID de l'auteur
     * @return Ensemble des auteurs du livre après le retrait
     */
    public Set<Author> removeAuthorFromBook(Long bookId, Long authorId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (!bookOpt.isPresent()) {
            throw new RuntimeException("Livre non trouvé avec l'ID : " + bookId);
        }

        Optional<Author> authorOpt = authorRepository.findById(authorId);
        if (!authorOpt.isPresent()) {
            throw new RuntimeException("Auteur non trouvé avec l'ID : " + authorId);
        }

        Book book = bookOpt.get();
        Author author = authorOpt.get();

        book.removeAuthor(author);
        bookRepository.save(book);

        return book.getAuthors();
    }

    // Méthodes pour gérer les catégories
    /**
     * Récupère les catégories d'un livre
     * 
     * @param bookId ID du livre
     * @return Ensemble des catégories du livre
     */
    public Set<CategoryDTO> getCategoriesByBook(Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            return book.get().getCategories().stream()
                    .map(this::convertCategoryToDTO)
                    .collect(Collectors.toSet());
        }
        throw new RuntimeException("Livre non trouvé avec l'ID : " + bookId);
    }

    /**
     * Ajoute une catégorie à un livre
     * 
     * @param bookId     ID du livre
     * @param categoryId ID de la catégorie
     * @return Ensemble des catégories du livre après l'ajout
     */
    public Set<Category> addCategoryToBook(Long bookId, Long categoryId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (!bookOpt.isPresent()) {
            throw new RuntimeException("Livre non trouvé avec l'ID : " + bookId);
        }

        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (!categoryOpt.isPresent()) {
            throw new RuntimeException("Catégorie non trouvée avec l'ID : " + categoryId);
        }

        Book book = bookOpt.get();
        Category category = categoryOpt.get();

        book.addCategory(category);
        bookRepository.save(book);

        return book.getCategories();
    }

    /**
     * Retire une catégorie d'un livre
     * 
     * @param bookId     ID du livre
     * @param categoryId ID de la catégorie
     * @return Ensemble des catégories du livre après le retrait
     */
    public Set<Category> removeCategoryFromBook(Long bookId, Long categoryId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (!bookOpt.isPresent()) {
            throw new RuntimeException("Livre non trouvé avec l'ID : " + bookId);
        }

        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (!categoryOpt.isPresent()) {
            throw new RuntimeException("Catégorie non trouvée avec l'ID : " + categoryId);
        }

        Book book = bookOpt.get();
        Category category = categoryOpt.get();

        book.removeCategory(category);
        bookRepository.save(book);

        return book.getCategories();
    }
}