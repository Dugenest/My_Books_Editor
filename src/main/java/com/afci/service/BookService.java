package com.afci.service;

import com.afci.data.Book;
import com.afci.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    /**
     * Récupère tous les livres de la base de données.
     * @param pageable 
     * @return Liste de tous les livres.
     */
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    /**
     * Recherche un livre par son identifiant.
     * @param id Identifiant du livre.
     * @return Un objet Optional contenant le livre s'il est trouvé.
     */
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    /**
     * Ajoute un nouveau livre à la base de données.
     * @param book Le livre à enregistrer.
     * @return Le livre enregistré.
     */
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Met à jour un livre existant dans la base de données.
     * @param book Le livre avec les nouvelles informations.
     * @return Le livre mis à jour.
     * @throws RuntimeException Si le livre avec l'ID donné n'existe pas.
     */
    public Book updateBook(Book book) {
        if (bookRepository.existsById(book.getId())) {
            return bookRepository.save(book);
        }
        throw new RuntimeException("Livre non trouvé avec l'ID : " + book.getId());
    }

    /**
     * Supprime un livre par son identifiant.
     * @param id Identifiant du livre à supprimer.
     */
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    /**
     * Recherche des livres dont le nom ou le prénom de l'auteur contient un texte donné (insensible à la casse).
     * @param lastName Nom de famille de l'auteur.
     * @param firstName Prénom de l'auteur.
     * @return Liste des livres correspondant à la recherche.
     */
    public List<Book> findByAuthorName(String lastName, String firstName) {
        return bookRepository.findByAuthor_LastNameContainingIgnoreCaseOrAuthor_FirstNameContainingIgnoreCase(lastName, firstName);
    }


    /**
     * Recherche des livres dont le titre contient un texte donné (insensible à la casse).
     * @param title Partie du titre à rechercher.
     * @return Liste des livres correspondant à la recherche.
     */
    public List<Book> findBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Recherche des livres appartenant à une catégorie spécifique.
     * @param category Nom de la catégorie.
     * @return Liste des livres de cette catégorie.
     */
    public List<Book> findBooksByCategory(String category) {
        return bookRepository.findByCategory_Name(category);
    }
    
    /**
     * Récupère les dernières parutions de livres
     * @param limit Le nombre de livres à récupérer
     * @return Une liste des derniers livres publiés
     */
    public List<Book> getNewReleases(int limit) {
        // Cette implémentation suppose que vous avez un champ 'publicationDate' ou similaire
        // Si ce n'est pas le cas, vous pouvez trier par ID (en supposant que les IDs plus élevés sont plus récents)
        Pageable pageable = PageRequest.of(0, limit, Sort.by("id").descending());
        return bookRepository.findAll(pageable).getContent();
    }
    
    /**
     * Récupère les livres les plus populaires
     * @param limit Le nombre de livres à récupérer
     * @return Une liste des livres les plus populaires
     */
    public List<Book> getPopularBooks(int limit) {
        // Cette implémentation est un placeholder
        // Idéalement, vous auriez un champ comme 'numberOfSales' ou 'rating' pour trier
        // Pour l'instant, on retourne simplement les premiers livres
        Pageable pageable = PageRequest.of(0, limit);
        return bookRepository.findAll(pageable).getContent();
    }
}
