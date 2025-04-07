package com.afci.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;

@Service
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${avatar.default-path:/api/files/default-avatar}")
    private String defaultAvatarPath;

    @Value("${avatar.upload-dir:${file.upload-dir}/avatars}")
    private String avatarUploadDir;

    /**
     * Stocke un fichier et retourne son nom unique
     * 
     * @param file     Le fichier à stocker
     * @param fileType Le type de fichier (pour la catégorisation)
     * @return Le nom unique du fichier stocké
     */
    @Operation(summary = "Store a file", description = "Store the uploaded file on the server with a unique name.")
    public String storeFile(MultipartFile file, String fileType) {
        String fileName = file.getOriginalFilename();

        try {
            if (fileName == null || fileName.contains("..")) {
                throw new FileSystemNotFoundException("Invalid path sequence in filename");
            }

            Path targetLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(targetLocation);

            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
            Path filePath = targetLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;
        } catch (IOException ex) {
            throw new FileSystemNotFoundException("Could not store file " + fileName);
        }
    }

    /**
     * Stocke un avatar et retourne son chemin d'accès pour l'API
     * 
     * @param file Le fichier d'avatar
     * @return Le chemin d'accès relatif du fichier stocké
     */
    public String storeAvatar(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        try {
            if (fileName == null || fileName.contains("..")) {
                throw new FileSystemNotFoundException("Invalid path sequence in filename");
            }

            // Créer le répertoire des avatars s'il n'existe pas
            Path targetLocation = Paths.get(avatarUploadDir).toAbsolutePath().normalize();
            Files.createDirectories(targetLocation);

            // Générer un nom de fichier unique
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String uniqueFileName = "avatar_" + UUID.randomUUID().toString() + extension;

            // Chemin complet du fichier
            Path filePath = targetLocation.resolve(uniqueFileName);

            // Copier le fichier
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Retourner le chemin API pour accéder au fichier
            return "/api/files/avatars/" + uniqueFileName;
        } catch (IOException ex) {
            throw new FileSystemNotFoundException("Could not store avatar file " + fileName);
        }
    }

    /**
     * Charge un fichier en tant que ressource
     * 
     * @param fileName Le nom du fichier à charger
     * @return La ressource correspondante au fichier
     * @throws FileNotFoundException Si le fichier n'est pas trouvé
     * @throws MalformedURLException Si l'URL du fichier est malformée
     */
    @Operation(summary = "Load a file as resource", description = "Load a file from the server and return it as a resource.")
    public Resource loadFileAsResource(String fileName) throws FileNotFoundException, MalformedURLException {
        try {
            Path filePath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("File not found " + fileName);
        }
    }

    /**
     * Charge un avatar en tant que ressource
     * 
     * @param fileName Le nom du fichier avatar à charger
     * @return La ressource correspondante au fichier
     * @throws FileNotFoundException Si le fichier n'est pas trouvé
     * @throws MalformedURLException Si l'URL du fichier est malformée
     */
    public Resource loadAvatarAsResource(String fileName) throws FileNotFoundException, MalformedURLException {
        try {
            Path filePath = Paths.get(avatarUploadDir).toAbsolutePath().normalize().resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("Avatar file not found " + fileName);
            }
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("Avatar file not found " + fileName);
        }
    }

    /**
     * Supprime un fichier
     * 
     * @param fileName Le nom du fichier à supprimer
     */
    @Operation(summary = "Delete a file", description = "Delete a file from the server by its file name.")
    public void deleteFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new FileSystemNotFoundException("Could not delete file " + fileName);
        }
    }

    /**
     * Supprime un fichier d'avatar par son chemin
     * 
     * @param avatarPath Le chemin d'accès du fichier d'avatar
     * @return true si le fichier a été supprimé, false sinon
     */
    public boolean deleteAvatar(String avatarPath) {
        // Ne pas supprimer l'avatar par défaut
        if (avatarPath == null || avatarPath.equals(defaultAvatarPath)) {
            return false;
        }

        try {
            // Extraire le nom du fichier du chemin API
            String fileName = extractFileNameFromPath(avatarPath);
            if (fileName == null) {
                return false;
            }

            Path filePath = Paths.get(avatarUploadDir).resolve(fileName);
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Extrait le nom du fichier à partir d'un chemin API
     * 
     * @param path Le chemin API du fichier
     * @return Le nom du fichier ou null si le chemin n'est pas valide
     */
    private String extractFileNameFromPath(String path) {
        if (path == null)
            return null;

        // Pour les chemins API comme /api/files/avatars/avatar_123.jpg
        if (path.startsWith("/api/files/avatars/")) {
            return path.substring("/api/files/avatars/".length());
        }

        // Pour les anciens chemins comme avatars/avatar_123.jpg
        if (path.startsWith("avatars/")) {
            return path.substring("avatars/".length());
        }

        // Si c'est juste le nom du fichier
        return path;
    }

    /**
     * Retourne le chemin d'accès de l'avatar par défaut
     * 
     * @return Le chemin d'accès API de l'avatar par défaut
     */
    public String getDefaultAvatarPath() {
        return defaultAvatarPath;
    }

    /**
     * Retourne le répertoire de stockage des avatars
     * 
     * @return Le chemin du répertoire de stockage des avatars
     */
    public String getAvatarUploadDir() {
        return avatarUploadDir;
    }
}