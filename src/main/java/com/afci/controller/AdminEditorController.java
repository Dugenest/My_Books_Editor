package com.afci.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.afci.data.Editor;
import com.afci.service.EditorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/editor")
@Tag(name = "Editor Administration", description = "Editor administrative operations")
public class AdminEditorController {

    @Autowired
    private EditorService editorService;

    @Operation(summary = "Get editor by ID for administration")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Editor>> getEditorById(
            @Parameter(description = "Editor ID") @PathVariable Long id) {
        return ResponseEntity.ok(editorService.getEditorById(id));
    }

    @Operation(summary = "Create editor with admin privileges")
    @PostMapping
    public ResponseEntity<Editor> createEditor(
            @Parameter(description = "Editor details") @Valid @RequestBody Editor editor) {
        return new ResponseEntity<>(editorService.createEditor(editor), HttpStatus.CREATED);
    }

    @Operation(summary = "Update editor with admin privileges")
    @PutMapping("/{id}")
    public ResponseEntity<Editor> updateEditor(
            @Parameter(description = "Editor ID") @PathVariable Long id,
            @Parameter(description = "Editor details") @Valid @RequestBody Editor editor) {
        return ResponseEntity.ok(editorService.updateEditor(editor));
    }

    @Operation(summary = "Delete editor with admin privileges")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEditor(
            @Parameter(description = "Editor ID") @PathVariable Long id) {
        editorService.deleteEditor(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get books by editor for administration")
    @GetMapping("/{id}/books")
    public ResponseEntity<Set<com.afci.data.Book>> getBooksByEditor(
            @Parameter(description = "Editor ID") @PathVariable Long id) {
        return ResponseEntity.ok(editorService.getBooksByEditor(id));
    }
} 