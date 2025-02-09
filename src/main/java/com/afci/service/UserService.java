package com.afci.service;

import com.afci.data.PasswordChangeRequest;
import com.afci.data.User;
import com.afci.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
        return userRepository.save(user);
    }

    // Mettre à jour un utilisateur
    public User updateUser(Long id, @Valid User user) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        user.setId(id); // Nous devons définir l'ID pour effectuer la mise à jour
        return userRepository.save(user);
    }

    // Supprimer un utilisateur
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        userRepository.deleteById(id);
    }

    // Changer le mot de passe d'un utilisateur
    public void changePassword(Long id, PasswordChangeRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        if (request.getOldPassword().equals(user.getPassword())) {
            user.setPassword(request.getNewPassword());
            userRepository.save(user);
        } else {
            throw new RuntimeException("Le mot de passe actuel est incorrect");
        }
    }
}
