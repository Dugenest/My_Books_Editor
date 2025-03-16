package com.afci.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.afci.data.Editor;
import com.afci.data.Book;
import com.afci.service.EditorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/editors")
@Tag(name = "Editor Management", description = "Editor operations")
public class EditorController {

    @Autowired
    private EditorService editorService;

    @Operation(summary = "Get all editors")
    @GetMapping
    public ResponseEntity<List<Editor>> getAllEditors() {
        return ResponseEntity.ok(editorService.getAllEditors());
    }

    @Operation(summary = "Get editor by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Editor>> getEditorById(
        @Parameter(description = "Editor ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok(editorService.getEditorById(id));
    }

    @Operation(summary = "Get books by editor")
    @GetMapping("/{id}/books")
    public ResponseEntity<Set<Book>> getBooksByEditor(
        @Parameter(description = "Editor ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok(editorService.getBooksByEditor(id));
    }

    @Operation(summary = "Create editor")
    @PostMapping
    public ResponseEntity<Editor> createEditor(
        @Parameter(description = "Editor details") @Valid @RequestBody Editor editor
    ) {
        return new ResponseEntity<>(editorService.createEditor(editor), 
            HttpStatus.CREATED);
    }

    @Operation(summary = "Update editor")
    @PutMapping("/{id}")
    public ResponseEntity<Editor> updateEditor(
        @Parameter(description = "Editor ID") @PathVariable Long id,
        @Parameter(description = "Editor details") @Valid @RequestBody Editor editor
    ) {
        return ResponseEntity.ok(editorService.updateEditor(editor));
    }

    @Operation(summary = "Delete editor")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEditor(
        @Parameter(description = "Editor ID") @PathVariable Long id
    ) {
        editorService.deleteEditor(id);
        return ResponseEntity.noContent().build();
    }

    // @Operation(summary = "Search editors")
    // @GetMapping("/search")
    // public ResponseEntity<List<Editor>> searchEditors(@RequestParam String query) {
    //     return ResponseEntity.ok(editorService.findByCompanyId(Long.parseLong(query)));
    // }
}