package com.afci.service;

import com.afci.data.Author;
import com.afci.repository.AuthorRepository;
import com.afci.data.Book;
import com.afci.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Page<Author> getAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    public Optional<Author> getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    public Author createAuthor(Author author) {
        // S'assurer que les champs sont correctement définis
        if (author.getFirstName() == null) {
            author.setFirstName("");
        }
        if (author.getLastName() == null) {
            author.setLastName("");
        }

        // Correction pour le champ authorNationality
        if (author.getAuthorNationality() == null || author.getAuthorNationality().isEmpty()) {
            author.setAuthorNationality("Non spécifiée");
        }

        // Définir le rôle AUTHOR
        author.setRole("AUTHOR");

        // Hacher le mot de passe avant de sauvegarder
        if (author.getPassword() != null && !author.getPassword().isEmpty()) {
            author.setPassword(passwordEncoder.encode(author.getPassword()));
        }

        return authorRepository.save(author);
    }

    public Author updateAuthor(Author author) {
        if (authorRepository.existsById(author.getId())) {
            // Récupérer l'auteur existant pour conserver les données non modifiées
            Author existingAuthor = authorRepository.findById(author.getId())
                    .orElseThrow(() -> new RuntimeException("Auteur non trouvé avec l'ID : " + author.getId()));

            // Mettre à jour les champs de base
            if (author.getFirstName() != null) {
                existingAuthor.setFirstName(author.getFirstName());
            }

            if (author.getLastName() != null) {
                existingAuthor.setLastName(author.getLastName());
            }

            // Vérifier si la nationalité doit être mise à jour
            if (author.getAuthorNationality() != null) {
                existingAuthor.setAuthorNationality(author.getAuthorNationality());
            } else if (existingAuthor.getAuthorNationality() == null
                    || existingAuthor.getAuthorNationality().isEmpty()) {
                existingAuthor.setAuthorNationality("Non spécifiée");
            }

            if (author.getBiography() != null) {
                existingAuthor.setBiography(author.getBiography());
            }

            if (author.getBirthDate() != null) {
                existingAuthor.setBirthDate(author.getBirthDate());
            }

            // Mettre à jour les champs hérités de User si nécessaire
            if (author.getEmail() != null) {
                existingAuthor.setEmail(author.getEmail());
            }

            if (author.getUsername() != null) {
                existingAuthor.setUsername(author.getUsername());
            }

            // Ne mettre à jour le mot de passe que s'il est fourni et non vide
            if (author.getPassword() != null && !author.getPassword().isEmpty()) {
                // Hacher le mot de passe avant de le sauvegarder
                existingAuthor.setPassword(passwordEncoder.encode(author.getPassword()));
            }

            if (author.getPhone() != null) {
                existingAuthor.setPhone(author.getPhone());
            }

            if (author.getAddress() != null) {
                existingAuthor.setAddress(author.getAddress());
            }

            System.out.println("Mise à jour de l'auteur: " + existingAuthor);

            try {
                return authorRepository.save(existingAuthor);
            } catch (Exception e) {
                System.err.println("Erreur lors de la mise à jour de l'auteur: " + e.getMessage());
                throw new RuntimeException("Erreur lors de la mise à jour de l'auteur: " + e.getMessage());
            }
        }
        throw new RuntimeException("Auteur non trouvé avec l'ID : " + author.getId());
    }

    // Les autres méthodes restent inchangées...

    public void deleteAuthor(Long id) {
        // Code inchangé
    }

    @Transactional
    public void forceDeleteAuthor(Long id) {
        // Code inchangé
    }

    public List<Author> findByName(String name) {
        return authorRepository.findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(name, name);
    }
}