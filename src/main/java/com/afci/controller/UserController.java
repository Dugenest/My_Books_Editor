package com.afci.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.afci.data.PasswordChangeRequest;
import com.afci.data.User;
import com.afci.dto.AddressDTO;
import com.afci.dto.UserDTO;
import com.afci.service.UserService;
import com.afci.service.WishlistService;
import com.afci.service.AuthorService;
import com.afci.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User operations")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private WishlistService wishlistService;

    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        try {
            Iterable<User> users = userService.getAllUsers();
            // Convertir Iterable en List pour pouvoir compter les éléments et les loguer
            List<User> userList = new java.util.ArrayList<>();
            users.forEach(userList::add);
            
            System.out.println("UserController: Nombre d'utilisateurs retournés: " + userList.size());
            
            // Log quelques détails pour le débogage
            if (userList.size() > 0) {
                User firstUser = userList.get(0);
                System.out.println("Premier utilisateur: ID=" + firstUser.getId() + ", Username=" + firstUser.getUsername());
            }
            
            return ResponseEntity.ok(userList);
        } catch (Exception e) {
            System.err.println("Exception dans UserController.getAllUsers: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erreur lors de la récupération des utilisateurs", 
                             "message", e.getMessage()));
        }
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

    @Operation(summary = "Update user")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(
            @Parameter(description = "User ID") @PathVariable Long id,
            @Parameter(description = "User details") @Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
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

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        // Récupérer l'email/username de l'utilisateur depuis l'authentification
        String email = authentication.getName();

        // Récupérer l'utilisateur à partir de l'email
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
        }

        // Convertir l'utilisateur en DTO pour éviter de renvoyer des données sensibles
        UserDTO userDTO = convertToDTO(user);

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/{userId}/stats")
    public ResponseEntity<?> getUserStats(@PathVariable Long userId) {
        // Créer un objet pour les statistiques utilisateur
        Map<String, Object> stats = new HashMap<>();

        // Remplir avec des données réelles à partir de vos services
        stats.put("orderCount", orderService.countByUserId(userId));
        stats.put("bookCount", orderService.countBooksByUserId(userId));
        stats.put("wishlistCount", wishlistService.countByUserId(userId));

        return ResponseEntity.ok(stats);
    }

    // Méthode auxiliaire pour convertir User en UserDTO
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setCreatedAt(user.getRegistrationDate());

        // Convertir l'adresse si elle existe
        if (user.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress());
            // Ajoutez d'autres propriétés si nécessaire
            dto.setAddress(addressDTO);
        }
        return dto;
    }

    @Operation(summary = "Get current user")
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User currentUser = userService.findByEmail(principal.getName());
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }

        // Masquer le mot de passe avant de renvoyer l'utilisateur
        currentUser.setPassword(null);
        return ResponseEntity.ok(currentUser);
    }
}
