package com.afci.controller;

import com.afci.data.UserProfile;
import com.afci.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user-profiles")
@CrossOrigin(origins = "*")
@Tag(name = "User Profile Management", description = "User profile operations")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @Operation(summary = "Get user profile")
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserProfile> getUserProfile(
        @Parameter(description = "User ID") @PathVariable Long userId
    ) {
        return userProfileService.getUserProfileById(userId)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Update user profile")
    @PutMapping("/user/{userId}")
    public ResponseEntity<UserProfile> updateUserProfile(
        @Parameter(description = "User ID") @PathVariable Long userId,
        @Parameter(description = "Profile details") @Valid @RequestBody UserProfile profile
    ) {
        profile.setId_profile(userId);  // S'assurer que l'ID est correct
        try {
            return ResponseEntity.ok(userProfileService.updateUserProfile(profile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Update profile picture")
    @PutMapping("/user/{userId}/picture")
    public ResponseEntity<UserProfile> updateProfilePicture(
        @Parameter(description = "User ID") @PathVariable Long userId,
        @Parameter(description = "Profile picture") @RequestParam("file") MultipartFile picture
    ) {
        try {
            return ResponseEntity.ok(userProfileService.updateProfilePicture(userId, picture));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Delete user profile")
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUserProfile(
        @Parameter(description = "User ID") @PathVariable Long userId
    ) {
        try {
            userProfileService.deleteUserProfile(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
