package com.afci.controller;

import com.afci.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
public class BookImageController {

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
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    @PostMapping("/{bookId}/image")
    public ResponseEntity<String> uploadBookImage(
            @PathVariable Long bookId,
            @RequestParam("picture") MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload a valid image");
            }

            // Validate file type (optional but recommended)
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed");
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String uniqueFilename = bookId + "_" +
                    UUID.randomUUID().toString() +
                    fileExtension;

            // Create full path
            Path targetLocation = Paths.get(uploadDir).resolve(uniqueFilename);

            // Copy file to target location
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Construct relative path for database storage
            String relativePath = "/uploads/book-covers/" + uniqueFilename;

            // Update book with image path in database
            bookService.updateBookImage(bookId, relativePath);

            return ResponseEntity.ok(relativePath);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not upload image: " + ex.getMessage());
        }
    }
}