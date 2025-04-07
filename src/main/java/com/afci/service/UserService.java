package com.afci.service;

import com.afci.data.PasswordChangeRequest;
import com.afci.data.User;
import com.afci.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileService fileService;

    // Récupérer tous les utilisateurs
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Récupérer un utilisateur par ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Récupérer un utilisateur par email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    // Créer un nouvel utilisateur
    public User createUser(@Valid User user) {
        // Vérifier si l'adresse mail existe
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }
        
        // Hacher le mot de passe avant de sauvegarder
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // Définir l'avatar par défaut si aucun n'est fourni
        if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            user.setAvatar(fileService.getDefaultAvatarPath());
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

        // Mettre à jour les nouveaux champs
        if (user.getNationality() != null) {
            existingUser.setNationality(user.getNationality());
        }

        if (user.getBirth_date() != null) {
            existingUser.setBirth_date(user.getBirth_date().getTime());
        }

        // Mettre à jour l'avatar si fourni et différent
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()
                && !user.getAvatar().equals(existingUser.getAvatar())) {
            // Si l'utilisateur avait déjà un avatar personnalisé, le supprimer
            if (existingUser.getAvatar() != null &&
                    !existingUser.getAvatar().equals(fileService.getDefaultAvatarPath())) {
                fileService.deleteAvatar(existingUser.getAvatar());
            }
            existingUser.setAvatar(user.getAvatar());
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
            throw new RuntimeException(
                    "Impossible de supprimer l'utilisateur car il a des livres associés. Veuillez d'abord supprimer ou réassigner ces livres.");
        }

        // Vérifier si l'utilisateur a des commandes associées
        if (user.getOrders() != null && !user.getOrders().isEmpty()) {
            throw new RuntimeException(
                    "Impossible de supprimer l'utilisateur car il a des commandes associées. Veuillez d'abord supprimer ces commandes.");
        }

        // Supprimer l'avatar si ce n'est pas l'avatar par défaut
        if (user.getAvatar() != null && !user.getAvatar().equals(fileService.getDefaultAvatarPath())) {
            fileService.deleteAvatar(user.getAvatar());
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

    /**
     * Met à jour l'avatar d'un utilisateur
     * 
     * @param id         L'ID de l'utilisateur
     * @param avatarFile Le fichier d'avatar
     * @return L'utilisateur mis à jour
     * @throws IOException si une erreur survient pendant le stockage du fichier
     */
    public User updateAvatar(Long id, MultipartFile avatarFile) throws IOException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Supprimer l'ancien avatar si ce n'est pas l'avatar par défaut
        if (user.getAvatar() != null && !user.getAvatar().equals(fileService.getDefaultAvatarPath())) {
            fileService.deleteAvatar(user.getAvatar());
        }

        // Stocker le nouvel avatar
        String avatarPath = fileService.storeAvatar(avatarFile);
        user.setAvatar(avatarPath);

        return userRepository.save(user);
    }

    /**
     * Réinitialise l'avatar d'un utilisateur à l'avatar par défaut
     * 
     * @param id L'ID de l'utilisateur
     * @return L'utilisateur mis à jour
     */
    public User resetAvatar(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Supprimer l'ancien avatar s'il n'est pas déjà l'avatar par défaut
        if (user.getAvatar() != null && !user.getAvatar().equals(fileService.getDefaultAvatarPath())) {
            fileService.deleteAvatar(user.getAvatar());
            // Définir l'avatar par défaut
            user.setAvatar(fileService.getDefaultAvatarPath());
            return userRepository.save(user);
        }

        return user; // Si l'avatar est déjà l'avatar par défaut, ne rien faire
    }

    // Sauvegarder l'avatar d'un utilisateur
    public void saveAvatar(Long userId, byte[] avatarBytes, String fileName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vous pouvez soit stocker l'avatar dans le système de fichiers
        // soit le convertir en Base64 pour le stocker directement dans la base de
        // données

        // Option 1: Stockage en Base64 (solution simple pour démarrer)
        String base64Avatar = java.util.Base64.getEncoder().encodeToString(avatarBytes);
        user.setAvatar(base64Avatar);

        // Option 2: Stockage dans le système de fichiers (à implémenter si nécessaire)
        // Stocker le fichier et enregistrer le chemin dans user.setAvatar(filepath)

        userRepository.save(user);
    }

    public User updateUserStatus(Long userId, boolean active) {
        User user = getUserById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setActive(active);
        return userRepository.save(user);
    }
}