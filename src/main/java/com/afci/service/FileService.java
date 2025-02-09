package com.afci.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @SuppressWarnings("null")
	@Operation(summary = "Store a file", description = "Store the uploaded file on the server with a unique name.")
    public String storeFile(MultipartFile file, String fileType) {
		String fileName = file.getOriginalFilename();

        try {
            if (fileName.contains("..")) {
                throw new FileSystemNotFoundException("Invalid path sequence in filename");
            }

            Path targetLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(targetLocation);

            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
            Path filePath = targetLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath);

            return uniqueFileName;
        } catch (IOException ex) {
            throw new FileSystemNotFoundException("Could not store file " + fileName);
        }
    }

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

    @Operation(summary = "Delete a file", description = "Delete a file from the server by its file name.")
    public void deleteFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new FileSystemNotFoundException("Could not delete file " + fileName);
        }
    }
}
