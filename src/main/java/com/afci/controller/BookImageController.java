package com.afci.controller;

import com.afci.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/books")
public class BookImageController {

    private static final Logger logger = LoggerFactory.getLogger(BookImageController.class);
    private final BookService bookService;
    private final String uploadDir;

    @Autowired
    public BookImageController(
            BookService bookService,
            @Value("${app.upload.dir:${user.home}/uploads/book-covers}") String uploadDir) {
        this.bookService = bookService;
        this.uploadDir = uploadDir;

        try {
            // Ensure upload directory exists
            Files.createDirectories(Paths.get(uploadDir));
            logger.info("Répertoire d'upload créé : {}", uploadDir);
        } catch (IOException e) {
            logger.error("Erreur lors de la création du répertoire d'upload", e);
            throw new RuntimeException("Impossible de créer le répertoire d'upload", e);
        }
    }

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadBookImage(
            @PathVariable("id") Long bookId,
            @RequestParam("file") MultipartFile file) {
        try {
            logger.info("Tentative d'upload d'image pour le livre {}", bookId);
            logger.info("Content-Type reçu : {}", file.getContentType());
            logger.info("Nom du fichier : {}", file.getOriginalFilename());
            logger.info("Taille du fichier : {} bytes", file.getSize());

            // Vérifier si un fichier a été fourni
            if (file == null || file.isEmpty()) {
                logger.warn("Aucun fichier n'a été fourni pour le livre {}", bookId);
                return ResponseEntity.badRequest().body("Veuillez fournir une image valide");
            }

            // Vérifier le type de fichier
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                logger.warn("Type de fichier invalide pour le livre {} : {}", bookId, contentType);
                return ResponseEntity.badRequest().body("Seuls les fichiers image sont autorisés");
            }

            // Générer un nom de fichier unique
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String uniqueFilename = bookId + "_" + System.currentTimeMillis() + fileExtension;

            logger.info("Nom de fichier généré : {}", uniqueFilename);

            // Créer le chemin complet
            Path targetLocation = Paths.get(uploadDir).resolve(uniqueFilename);
            logger.info("Chemin cible : {}", targetLocation.toAbsolutePath());

            // Vérifier si le répertoire existe
            Path uploadPath = Paths.get(uploadDir);
            boolean exists = Files.exists(uploadPath);
            boolean writable = Files.isWritable(uploadPath);
            logger.info("Répertoire d'upload existe: {}, est accessible en écriture: {}", exists, writable);

            if (!exists) {
                Files.createDirectories(uploadPath);
                logger.info("Répertoire d'upload créé: {}", uploadPath);
            }

            // Copier le fichier vers l'emplacement cible
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Fichier copié avec succès vers : {}", targetLocation);

            // Construire le chemin relatif pour le stockage en base de données
            String relativePath = "/uploads/book-covers/" + uniqueFilename;

            // Mettre à jour le livre avec le chemin de l'image en base de données
            bookService.updateBookImage(bookId, relativePath);
            logger.info("Image mise à jour pour le livre {} : {}", bookId, relativePath);

            return ResponseEntity.ok(relativePath);
        } catch (IOException ex) {
            logger.error("Erreur lors de l'upload de l'image pour le livre " + bookId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'upload de l'image : " + ex.getMessage());
        }
    }
}