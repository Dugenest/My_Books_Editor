package com.afci.service;

import com.afci.data.PasswordChangeRequest;
import com.afci.data.User;
import com.afci.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
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
        try {
            // Requête SQL native directe qui évite le mécanisme de discrimination
            List<Map<String, Object>> rawUsers = userRepository.findAllUsersRaw();
            List<User> users = new java.util.ArrayList<>();
            
            for (Map<String, Object> rawUser : rawUsers) {
                try {
                    User user = new User();
                    user.setId(((Number) rawUser.get("id")).longValue());
                    user.setUsername((String) rawUser.get("username"));
                    // Ne pas définir le mot de passe pour des raisons de sécurité
                    user.setEmail((String) rawUser.get("email"));
                    user.setFirstName((String) rawUser.get("first_name"));
                    user.setLastName((String) rawUser.get("last_name"));
                    
                    // Gérer les valeurs potentiellement nulles
                    Boolean subscribedToNewsletter = (Boolean) rawUser.get("subscribed_to_newsletter");
                    user.setSubscribedToNewsletter(subscribedToNewsletter != null ? subscribedToNewsletter : false);
                    
                    user.setPhone((String) rawUser.get("phone"));
                    user.setAddress((String) rawUser.get("address"));
                    user.setRole((String) rawUser.get("role"));
                    user.setRegistrationDate((java.util.Date) rawUser.get("registration_date"));
                    
                    Boolean active = (Boolean) rawUser.get("active");
                    user.setActive(active != null ? active : true);
                    
                    users.add(user);
                } catch (Exception e) {
                    // Log l'erreur mais continue avec les autres utilisateurs
                    System.err.println("Erreur lors de la conversion d'un utilisateur brut: " + e.getMessage());
                }
            }
            
            System.out.println("Nombre d'utilisateurs récupérés: " + users.size());
            return users;
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des utilisateurs: " + e.getMessage());
            e.printStackTrace();
            
            // Dernière tentative avec JPQL directe
            try {
                List<User> users = userRepository.findAllValidUsers();
                System.out.println("Nombre d'utilisateurs récupérés via JPQL: " + users.size());
                return users;
            } catch (Exception e2) {
                System.err.println("Erreur avec la requête JPQL: " + e2.getMessage());
                e2.printStackTrace();
                return new java.util.ArrayList<>(); // Retourner une liste vide en dernier recours
            }
        }
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

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email : " + email));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec le nom d'utilisateur: " + username));
    }
}