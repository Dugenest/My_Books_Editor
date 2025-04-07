package com.afci.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class AvatarController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${avatar.default-path:assets/default-avatar.png}")
    private String defaultAvatarPath;

    @GetMapping("/uploads/avatars/{filename:.+}")
    public ResponseEntity<Resource> serveAvatar(@PathVariable String filename) {
        try {
            // Si on demande explicitement l'avatar par défaut
            if ("default-avatar.png".equals(filename)) {
                Resource defaultResource = new ClassPathResource("static/" + defaultAvatarPath);
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(defaultResource);
            }

            // Sinon, on cherche l'avatar demandé
            Path filePath = Paths.get(uploadDir + "/avatars/" + filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "image/jpeg";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                // Si l'avatar demandé n'existe pas, on renvoie l'avatar par défaut
                Resource defaultResource = new ClassPathResource("static/" + defaultAvatarPath);
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(defaultResource);
            }
        } catch (IOException e) {
            // En cas d'erreur, on renvoie aussi l'avatar par défaut
            try {
                Resource defaultResource = new ClassPathResource("static/" + defaultAvatarPath);
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(defaultResource);
            } catch (Exception ex) {
                return ResponseEntity.notFound().build();
            }
        }
    }

    // Ajoutez un endpoint pour l'avatar par défaut
    @GetMapping("/default-avatar")
    public ResponseEntity<Resource> serveDefaultAvatar() {
        try {
            Resource defaultResource = new ClassPathResource("static/" + defaultAvatarPath);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(defaultResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}