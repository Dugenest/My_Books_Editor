package com.afci.service;

import com.afci.data.Book;
import com.afci.data.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    /**
     * Récupère tous les livres de la base de données.
     * @return Liste de tous les livres.
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
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
}
