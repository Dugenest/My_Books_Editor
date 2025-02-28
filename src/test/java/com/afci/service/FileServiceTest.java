package com.afci.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private MultipartFile multipartFile;

    private String uploadDir = "src/test/resources/uploads";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(fileService, "uploadDir", uploadDir);
        // Créer le répertoire de test s'il n'existe pas
        Path directory = Paths.get(uploadDir);
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            fail("Impossible de créer le répertoire de test");
        }
    }

    @Test
    void storeFile_ShouldSaveFileAndReturnFileName() throws IOException {
        // Arrange
        String fileName = "test.txt";
        byte[] content = "test content".getBytes();
        when(multipartFile.getOriginalFilename()).thenReturn(fileName);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(content));

        // Act
        String savedFileName = fileService.storeFile(multipartFile, "text");

        // Assert
        assertTrue(savedFileName.endsWith(fileName));
        assertTrue(savedFileName.contains("_"));
        
        // Cleanup
        Path filePath = Paths.get(uploadDir).resolve(savedFileName);
        Files.deleteIfExists(filePath);
    }

    @Test
    void storeFile_WithInvalidPath_ShouldThrowException() {
        // Arrange
        when(multipartFile.getOriginalFilename()).thenReturn("../test.txt");

        // Act & Assert
        assertThrows(FileSystemNotFoundException.class, 
            () -> fileService.storeFile(multipartFile, "text"));
    }

    @Test
    void loadFileAsResource_ExistingFile_ShouldReturnResource() throws IOException {
        // Arrange
        String fileName = "test.txt";
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        Files.write(filePath, "test content".getBytes());

        // Act
        Resource resource = assertDoesNotThrow(() -> fileService.loadFileAsResource(fileName));

        // Assert
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());

        // Cleanup
        Files.deleteIfExists(filePath);
    }

    @Test
    void loadFileAsResource_NonExistingFile_ShouldThrowException() {
        // Act & Assert
        assertThrows(FileNotFoundException.class, 
            () -> fileService.loadFileAsResource("nonexistent.txt"));
    }

    @Test
    void deleteFile_ExistingFile_ShouldDeleteFile() throws IOException {
        // Arrange
        String fileName = "test.txt";
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        Files.write(filePath, "test content".getBytes());

        // Act
        assertDoesNotThrow(() -> fileService.deleteFile(fileName));

        // Assert
        assertFalse(Files.exists(filePath));
    }
} 