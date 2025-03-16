package com.afci.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.afci.data.LoginRequest;
import com.afci.data.RegistrationRequest;
import com.afci.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Opérations d'authentification")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Inscrire un nouvel utilisateur")
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Parameter(description = "Données d'inscription") @Valid @RequestBody RegistrationRequest request) {
        
        try {
            authService.registerUser(request);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Inscription réussie! Veuillez vérifier votre email pour activer votre compte.");
            response.put("email", request.getEmail());
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Connecter un utilisateur")
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Parameter(description = "Identifiants de connexion") @Valid @RequestBody LoginRequest request) {
        
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }
    
    @Operation(summary = "Confirmer l'email d'un utilisateur")
    @GetMapping("/confirm")
    public ResponseEntity<?> confirmEmail(
            @Parameter(description = "Token de confirmation") @RequestParam("token") String token) {
        
        try {
            authService.confirmEmail(token);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Votre compte a été activé avec succès. Vous pouvez maintenant vous connecter.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
    
    @Operation(summary = "Renvoyer l'email de confirmation")
    @PostMapping("/resend-confirmation")
    public ResponseEntity<?> resendConfirmation(
            @Parameter(description = "Email de l'utilisateur") @RequestBody Map<String, String> request) {
        
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "L'email est obligatoire");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        
        try {
            authService.resendConfirmationEmail(email);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Si votre compte existe et n'est pas encore activé, un nouvel email de confirmation a été envoyé.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}