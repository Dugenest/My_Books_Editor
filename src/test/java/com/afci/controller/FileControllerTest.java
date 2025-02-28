package com.afci.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.afci.data.FileResponse;
import com.afci.service.FileService;

@ExtendWith(MockitoExtension.class)
public class FileControllerTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    private MockMultipartFile multipartFile;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        multipartFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "test content".getBytes());
        request = new MockHttpServletRequest();
        // Configurer le contexte de requête
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void uploadFile_ShouldReturnFileResponse() {
        // Arrange
        String fileName = "stored_test.txt";
        when(fileService.storeFile(any(MultipartFile.class), eq("document")))
                .thenReturn(fileName);

        // Act
        ResponseEntity<FileResponse> response = fileController.uploadFile(multipartFile, "document");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(fileName, response.getBody().getFileName());
    }

    @Test
    void downloadFile_ShouldReturnResource() throws FileNotFoundException, IOException {
        // Arrange
        Resource mockResource = mock(Resource.class);
        // Supprimez cette ligne qui cause l'erreur
        // when(mockResource.exists()).thenReturn(true);
        
        when(mockResource.getFilename()).thenReturn("test.txt");
        
        File mockFile = mock(File.class);
        when(mockFile.getAbsolutePath()).thenReturn("/path/to/test.txt");
        when(mockResource.getFile()).thenReturn(mockFile);
        
        when(fileService.loadFileAsResource("test.txt")).thenReturn(mockResource);
    
        // Act
        ResponseEntity<Resource> response = 
            fileController.downloadFile("test.txt", request);
    
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deleteFile_ShouldReturnOkResponse() {
        // Arrange
        doNothing().when(fileService).deleteFile("test.txt");

        // Act
        ResponseEntity<Void> response = fileController.deleteFile("test.txt");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void uploadMultipleFiles_ShouldReturnFileResponses() {
        // Arrange
        MultipartFile[] files = { multipartFile, multipartFile };
        when(fileService.storeFile(any(MultipartFile.class), eq("document")))
                .thenReturn("stored_test.txt");

        // Act
        ResponseEntity<List<FileResponse>> response = fileController.uploadMultipleFiles(files, "document");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @AfterEach
    void tearDown() {
        // Nettoyer le contexte après chaque test
        RequestContextHolder.resetRequestAttributes();
    }
}