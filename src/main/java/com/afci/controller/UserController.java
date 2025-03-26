package com.afci.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.afci.data.PasswordChangeRequest;
import com.afci.data.User;
import com.afci.service.UserService;
import com.afci.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User operations")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorService authorService;

    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(
        @Parameter(description = "User ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Create user")
    @PostMapping
    public ResponseEntity<User> createUser(
        @Parameter(description = "User details") @Valid @RequestBody User user
    ) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @Operation(summary = "Update user")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(
        @Parameter(description = "User ID") @PathVariable Long id,
        @Parameter(description = "User details") @Valid @RequestBody User user
    ) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
        @Parameter(description = "User ID") @PathVariable Long id
    ) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                    "error", "Erreur lors de la suppression",
                    "message", e.getMessage()
                ));
        }
    }

    @Operation(summary = "Change user password")
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
        @Parameter(description = "User ID") @PathVariable Long id,
        @Parameter(description = "Password details") @Valid @RequestBody PasswordChangeRequest request
    ) {
        userService.changePassword(id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Force delete author by ID")
    @DeleteMapping("/force-delete-by-author/{id}")
    public ResponseEntity<?> forceDeleteByAuthor(
        @Parameter(description = "Author ID") @PathVariable Long id
    ) {
        try {
            authorService.forceDeleteAuthor(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                    "error", "Erreur lors de la suppression forcée",
                    "message", e.getMessage()
                ));
        }
    }

    @GetMapping("/me")
public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
    // Récupérer l'utilisateur authentifié par son nom d'utilisateur
    String username = userDetails.getUsername();
    User user = userService.findByUsername(username);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
    // Retourner les informations de l'utilisateur
    return ResponseEntity.ok(user);
}
}
