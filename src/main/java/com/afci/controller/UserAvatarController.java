package com.afci.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.afci.data.User;
import com.afci.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Avatar Management", description = "User avatar operations")
public class UserAvatarController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Upload user avatar")
    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(
            @Parameter(description = "User ID") @PathVariable Long id,
            @Parameter(description = "Avatar file") @RequestParam("avatar") MultipartFile avatarFile) {
        try {
            if (avatarFile.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Erreur de fichier",
                        "message", "Le fichier est vide"));
            }

            // Vérifier le type MIME
            String contentType = avatarFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Type de fichier non valide",
                        "message", "Seules les images sont acceptées"));
            }

            User updatedUser = userService.updateAvatar(id, avatarFile);
            return ResponseEntity.ok(updatedUser);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Erreur lors de l'upload",
                    "message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Erreur utilisateur",
                    "message", e.getMessage()));
        }
    }

    @Operation(summary = "Reset avatar to default")
    @DeleteMapping("/{id}/avatar")
    public ResponseEntity<?> resetAvatar(
            @Parameter(description = "User ID") @PathVariable Long id) {
        try {
            User updatedUser = userService.resetAvatar(id);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Erreur utilisateur",
                    "message", e.getMessage()));
        }
    }
}
