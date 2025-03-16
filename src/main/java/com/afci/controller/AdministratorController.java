package com.afci.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;

import com.afci.data.Administrator;
import com.afci.service.AdministratorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/administrators")
@Tag(name = "Administrator Management", description = "Administrator operations")
public class AdministratorController {

    @Autowired
    private AdministratorService administratorService;

    @Operation(summary = "Get all administrators")
    @GetMapping
    public ResponseEntity<List<Administrator>> getAllAdministrators() {
        return ResponseEntity.ok(administratorService.getAllAdministrators());
    }

    @Operation(summary = "Get administrator by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Administrator> getAdministratorById(@PathVariable Long id) {
        Optional<Administrator> administratorOpt = administratorService.getAdministratorById(id);
        return administratorOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @Operation(summary = "Create administrator")
    @PostMapping
    public ResponseEntity<Administrator> createAdministrator(
        @Parameter(description = "Administrator details") 
        @Valid @RequestBody Administrator administrator
    ) {
        return new ResponseEntity<>(
            administratorService.createAdministrator(administrator), 
            HttpStatus.CREATED
        );
    }

    @Operation(summary = "Update administrator")
    @PutMapping("/{id}")
    public ResponseEntity<Administrator> updateAdministrator(
        @Parameter(description = "Administrator ID") @PathVariable Long id,
        @Parameter(description = "Administrator details") 
        @Valid @RequestBody Administrator administrator
    ) {
        return ResponseEntity.ok(administratorService.updateAdministrator(administrator));
    }

    @Operation(summary = "Delete administrator")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrator(
        @Parameter(description = "Administrator ID") @PathVariable Long id
    ) {
        administratorService.deleteAdministrator(id);
        return ResponseEntity.noContent().build();
    }
}// }