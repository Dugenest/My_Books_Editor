package com.afci.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.afci.data.FileResponse;
import com.afci.service.FileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@Tag(name = "File Management", description = "File upload and download operations")
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private FileService fileService;

    @Operation(summary = "Upload file")
    @PostMapping("/upload")
    public ResponseEntity<FileResponse> uploadFile(
        @Parameter(description = "File to upload") 
        @RequestParam MultipartFile file,
        @Parameter(description = "File type") 
        @RequestParam("type") String fileType
    ) {
        String fileName = fileService.storeFile(file, fileType);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/files/download/")
            .path(fileName)
            .toUriString();

        return ResponseEntity.ok(new FileResponse(fileName, fileDownloadUri, 
            file.getContentType(), file.getSize()));
    }

    @Operation(summary = "Download file")
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(
        @Parameter(description = "File name") @PathVariable String fileName, 
        HttpServletRequest request
    ) throws FileNotFoundException, MalformedURLException {
        Resource resource = fileService.loadFileAsResource(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext()
                .getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            // Logger.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, 
                "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }

    @Operation(summary = "Delete file")
    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> deleteFile(
        @Parameter(description = "File name") @PathVariable String fileName
    ) {
        fileService.deleteFile(fileName);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Upload multiple files")
    @PostMapping("/upload-multiple")
    public ResponseEntity<List<FileResponse>> uploadMultipleFiles(
        @Parameter(description = "Files to upload") 
        @RequestParam MultipartFile[] files,
        @Parameter(description = "File type") 
        @RequestParam("type") String fileType
    ) {
        List<FileResponse> responses = new ArrayList<>();
        
        for (MultipartFile file : files) {
            String fileName = fileService.storeFile(file, fileType);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/download/")
                .path(fileName)
                .toUriString();
                
            responses.add(new FileResponse(fileName, fileDownloadUri, 
                file.getContentType(), file.getSize()));
        }

        return ResponseEntity.ok(responses);
    }
}
