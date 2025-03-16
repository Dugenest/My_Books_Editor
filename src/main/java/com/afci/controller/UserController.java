package com.afci.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.afci.data.PasswordChangeRequest;
import com.afci.data.User;
import com.afci.service.UserService;
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
    public ResponseEntity<Void> deleteUser(
        @Parameter(description = "User ID") @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
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
}
