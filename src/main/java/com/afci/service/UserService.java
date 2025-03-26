package com.afci.service;

import com.afci.data.PasswordChangeRequest;
import com.afci.data.User;
import com.afci.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Récupérer tous les utilisateurs
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Récupérer un utilisateur par ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Créer un nouvel utilisateur
    public User createUser(@Valid User user) {
        // Hacher le mot de passe avant de sauvegarder
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    // Mettre à jour un utilisateur
    public User updateUser(Long id, @Valid User user) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé");
        }

        // Récupérer l'utilisateur existant pour vérifier le mot de passe
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Mettre à jour les propriétés de l'utilisateur existant
        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }
        
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        
        if (user.getFirstName() != null) {
            existingUser.setFirstName(user.getFirstName());
        }
        
        if (user.getLastName() != null) {
            existingUser.setLastName(user.getLastName());
        }
        
        if (user.getPhone() != null) {
            existingUser.setPhone(user.getPhone());
        }
        
        if (user.getAddress() != null) {
            existingUser.setAddress(user.getAddress());
        }
        
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }
        
        // Gérer le mot de passe séparément
        if (user.getPassword() != null && !user.getPassword().isEmpty()
                && !user.getPassword().equals(existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        // Mettre à jour les champs booléens
        existingUser.setActive(user.isActive());
        existingUser.setSubscribedToNewsletter(user.isSubscribedToNewsletter());
        
        // Conserver la date d'enregistrement si elle existe déjà
        if (existingUser.getRegistrationDate() == null && user.getRegistrationDate() != null) {
            existingUser.setRegistrationDate(user.getRegistrationDate());
        }

        // Sauvegarder l'utilisateur existant avec les propriétés mises à jour
        return userRepository.save(existingUser);
    }

    // Supprimer un utilisateur
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        
        // Récupérer l'utilisateur pour vérifier s'il a des relations
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // Vérifier si l'utilisateur a des livres associés
        if (user.getBooks() != null && !user.getBooks().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer l'utilisateur car il a des livres associés. Veuillez d'abord supprimer ou réassigner ces livres.");
        }
        
        // Vérifier si l'utilisateur a des commandes associées
        if (user.getOrders() != null && !user.getOrders().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer l'utilisateur car il a des commandes associées. Veuillez d'abord supprimer ces commandes.");
        }
        
        userRepository.deleteById(id);
    }

    // Forcer la suppression d'un auteur par son ID
    public void forceDeleteByAuthorId(Long authorId) {
        // Code inchangé
    }

    // Changer le mot de passe d'un utilisateur
    public void changePassword(Long id, PasswordChangeRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier l'ancien mot de passe en utilisant le PasswordEncoder
        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            // Hacher le nouveau mot de passe
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new RuntimeException("Le mot de passe actuel est incorrect");
        }
    }

    // Trouver un utilisateur par son nom d'utilisateur
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}