package com.afci.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.afci.data.PasswordChangeRequest;
import com.afci.data.User;
import com.afci.service.AuthorService;
import com.afci.service.FileService;
import com.afci.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User operations")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private FileService fileService;

    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(
            @Parameter(description = "User ID") @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Create user")
    @PostMapping
    public ResponseEntity<User> createUser(
            @Parameter(description = "User details") @Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> createUserWithAvatar(
            @RequestParam("userData") String userJson,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile) {
        try {
            // Parser les données utilisateur depuis JSON
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(userJson, User.class);

            // Créer l'utilisateur d'abord
            User createdUser = userService.createUser(user);

            // Traiter l'avatar si présent
            if (avatarFile != null && !avatarFile.isEmpty()) {
                String avatarPath = fileService.storeAvatar(avatarFile);
                createdUser.setAvatar(avatarPath);
                createdUser = userService.updateUser(createdUser.getId(), createdUser);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    @Operation(summary = "Update user with JSON")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateUserJson(
            @Parameter(description = "User ID") @PathVariable Long id,
            @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erreur de mise à jour", "message", e.getMessage()));
        }
    }

    @Operation(summary = "Update user with multipart form")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateUserForm(
            @Parameter(description = "User ID") @PathVariable Long id,
            @RequestParam(value = "userData", required = true) String userJson,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile) {

        try {
            // Parse user data from JSON
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(userJson, User.class);

            // Update user data first
            User updatedUser = userService.updateUser(id, user);

            // Handle avatar file if present
            if (avatarFile != null && !avatarFile.isEmpty()) {
                try {
                    // Logique pour sauvegarder l'avatar
                    byte[] avatarBytes = avatarFile.getBytes();
                    userService.saveAvatar(id, avatarBytes, avatarFile.getOriginalFilename());
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("error", "Erreur lors du traitement de l'avatar", "message", e.getMessage()));
                }
            }

            return ResponseEntity.ok(updatedUser);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors du traitement de la requête", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erreur de mise à jour", "message", e.getMessage()));
        }
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "User ID") @PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Erreur lors de la suppression",
                            "message", e.getMessage()));
        }
    }

    @Operation(summary = "Change user password")
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "User ID") @PathVariable Long id,
            @Parameter(description = "Password details") @Valid @RequestBody PasswordChangeRequest request) {
        userService.changePassword(id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Force delete author by ID")
    @DeleteMapping("/force-delete-by-author/{id}")
    public ResponseEntity<?> forceDeleteByAuthor(
            @Parameter(description = "Author ID") @PathVariable Long id) {
        try {
            authorService.forceDeleteAuthor(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Erreur lors de la suppression forcée",
                            "message", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        // Récupérer l'authentification du contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Récupérer l'email de l'utilisateur depuis l'authentification
            String userEmail = authentication.getName();

            // Rechercher l'utilisateur par email au lieu de par nom d'utilisateur
            try {
                User user = userService.getUserByEmail(userEmail);
                return ResponseEntity.ok(user);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Erreur lors de la récupération de l'utilisateur"));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Utilisateur non authentifié"));
    }

    @GetMapping("/me/role")
    public ResponseEntity<?> getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String userEmail = authentication.getName();
            try {
                User user = userService.getUserByEmail(userEmail);
                Map<String, Object> response = new HashMap<>();
                response.put("role", user.getRole());
                response.put("roles", authentication.getAuthorities().stream()
                    .map(auth -> auth.getAuthority())
                    .toList());
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de la récupération du rôle"));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "Utilisateur non authentifié"));
    }

    @GetMapping("/{userId}/stats")
    public ResponseEntity<Map<String, Object>> getUserStats(@PathVariable Long userId) {
        try {
            Map<String, Object> stats = new HashMap<>();

            // Récupérer l'utilisateur
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            // Ajouter les statistiques de base de l'utilisateur
            stats.put("registrationDate", user.getRegistrationDate());
            stats.put("totalOrders", 0); // À remplacer par des données réelles lorsqu'elles seront disponibles
            stats.put("totalSpent", 0.0); // À remplacer par des données réelles lorsqu'elles seront disponibles
            stats.put("lastLogin", new Date(System.currentTimeMillis())); // À remplacer par des données réelles

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de la récupération des statistiques utilisateur", "message",
                            e.getMessage()));
        }
    }

    @Operation(summary = "Update user status")
    @PutMapping("/{userId}/status")
    public ResponseEntity<?> updateUserStatus(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @RequestBody Map<String, Boolean> statusUpdate) {
        try {
            boolean active = statusUpdate.get("active");
            User updatedUser = userService.updateUserStatus(userId, active);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Erreur lors de la mise à jour du statut",
                            "message", e.getMessage()));
        }
    }
}