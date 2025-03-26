package com.afci.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afci.data.Serial;
import com.afci.service.SerialService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/serials")
@Tag(name = "Serial Management", description = "Serial/Series operations")
public class SerialController {

    @Autowired
    private SerialService serialService;

    @Operation(summary = "Get all serials")
    @GetMapping
    public ResponseEntity<List<Serial>> getAllSerials() {
        return ResponseEntity.ok(serialService.getAllSerials());
    }

    @Operation(summary = "Get serial by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Serial>> getSerialById(
            @Parameter(description = "Serial ID") @PathVariable Long id) {
        return ResponseEntity.ok(serialService.getSerialById(id));
    }

    @Operation(summary = "Get books in serial")
    @GetMapping("/{id}/books")
    public ResponseEntity<Optional<Serial>> getSerialBooks(
            @Parameter(description = "Serial ID") @PathVariable Long id) {
        return ResponseEntity.ok(serialService.getSerialById(id));
    }

    @Operation(summary = "Create serial")
    @PostMapping
    public ResponseEntity<Serial> createSerial(
            @Parameter(description = "Serial details") @Valid @RequestBody Serial serial) {
        return new ResponseEntity<>(serialService.createSerial(serial),
                HttpStatus.CREATED);
    }

    @Operation(summary = "Update serial")
    @PutMapping("/{id}")
    public ResponseEntity<Serial> updateSerial(
            @Parameter(description = "Serial ID") @PathVariable Long id,
            @Parameter(description = "Serial details") @Valid @RequestBody Serial serial) {
        return ResponseEntity.ok(serialService.updateSerial(serial));
    }

    @Operation(summary = "Delete serial")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSerial(
            @Parameter(description = "Serial ID") @PathVariable Long id) {
        serialService.deleteSerial(id);
        return ResponseEntity.noContent().build();
    }
}