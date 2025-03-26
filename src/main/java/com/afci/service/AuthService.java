package com.afci.service;

import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.afci.data.LoginRequest;
import com.afci.data.RegistrationRequest;
import com.afci.data.User;
import com.afci.repository.UserRepository;
import com.afci.security.JwtProvider;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtProvider jwtProvider;
    
    @Autowired
    private EmailService emailService;

    /**
     * Méthode pour inscrire un nouvel utilisateur
     */
    public User registerUser(RegistrationRequest request) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Cette adresse email est déjà utilisée");
        }

        // Créer un nouvel utilisateur
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUsername(request.getEmail()); 
        user.setActive(false); // L'utilisateur doit confirmer son email
        user.setRole("USER"); // Rôle par défaut
        
        // Définir la propriété newsletter si disponible
        try {
            user.setSubscribedToNewsletter(request.isSubscribeNewsletter());
        } catch (Exception e) {
            // Si cette propriété n'est pas disponible, ignorer l'erreur
        }

        // Sauvegarder l'utilisateur
        User savedUser = userRepository.save(user);
        
        // Générer un token et envoyer l'email de confirmation
        String token = generateToken(user.getEmail());
        emailService.sendConfirmationEmail(user.getEmail(), token);
        
        return savedUser;
    }

    /**
     * Méthode pour connecter un utilisateur
     */
    public Map<String, Object> login(LoginRequest request) {
        logger.info("Tentative de connexion pour l'email: {}", request.getEmail());
        
        // Rechercher l'utilisateur par email
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        
        if (userOpt.isEmpty()) {
            logger.warn("Échec de connexion: Utilisateur non trouvé pour l'email {}", request.getEmail());
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }
        
        User user = userOpt.get();
        logger.debug("Utilisateur trouvé: ID={}, Email={}, Active={}", user.getId(), user.getEmail(), user.isActive());
        
        // Vérifier le mot de passe
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.warn("Échec de connexion: Mot de passe incorrect pour l'email {}", request.getEmail());
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }
        
        // Vérifier si le compte est actif
        if (!user.isActive()) {
            logger.warn("Échec de connexion: Compte inactif pour l'email {}", request.getEmail());
            throw new RuntimeException("Veuillez confirmer votre compte en cliquant sur le lien envoyé par email");
        }
        
        logger.info("Authentification réussie pour l'utilisateur: {}", user.getEmail());
        
        // Créer un objet UserDetails pour l'authentification
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );
        
        // Important : utiliser un objet UserDetails au lieu d'une chaîne
        org.springframework.security.core.userdetails.User userDetails = 
            new org.springframework.security.core.userdetails.User(
                user.getEmail(), 
                user.getPassword(), // Utilisez le mot de passe encodé, il ne sera pas utilisé car c'est juste pour le token
                user.isActive(),
                true, true, true,
                authorities
            );
        
        // Créer l'objet Authentication avec UserDetails
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, authorities);
        
        // Générer un token JWT avec JwtProvider
        String token = jwtProvider.generateToken(authentication);
        logger.debug("Token JWT généré pour l'utilisateur: {}", user.getEmail());
        
        // Préparer la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        
        // Masquer le mot de passe avant de renvoyer l'utilisateur
        user.setPassword(null);
        response.put("user", user);
        
        return response;
    }
    
    /**
     * Méthode pour renvoyer un email de confirmation
     */
    public void resendConfirmationEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email); 
        
        if (userOpt.isEmpty()) {
            // Ne pas révéler si l'email existe ou non pour des raisons de sécurité
            return;
        }
        
        User user = userOpt.get();
        
        if (user.isActive()) {
            // Le compte est déjà actif
            return;
        }
        
        // Générer un nouveau token et envoyer l'email
        String token = generateToken(user.getEmail());
        emailService.sendConfirmationEmail(user.getEmail(), token);
    }
    
    /**
     * Méthode pour confirmer l'email d'un utilisateur
     */
    public void confirmEmail(String token) {
        try {
            String email = decodeEmailFromToken(token);
            Optional<User> userOpt = userRepository.findByEmail(email);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setActive(true);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Utilisateur non trouvé");
            }
        } catch (Exception e) {
            throw new RuntimeException("Token invalide ou expiré");
        }
    }
    
    /**
     * Génère un token JWT pour l'authentification
     */
    private String generateJwtToken(User user) {
        // En production, vous devriez utiliser une bibliothèque JWT comme jjwt
        // Pour l'instant, un token simple basé sur l'ID et un timestamp
        return Base64.getEncoder().encodeToString(
            (user.getId() + ":" + user.getEmail() + ":" + System.currentTimeMillis()).getBytes()
        );
    }
    
    /**
     * Génère un token pour la confirmation d'email
     * En production, ce token devrait être stocké en base de données avec une date d'expiration
     */
    private String generateToken(String email) {
        // Combinaison de l'email et d'un UUID pour plus de sécurité
        String tokenData = email + ":" + UUID.randomUUID().toString();
        return Base64.getEncoder().encodeToString(tokenData.getBytes());
    }
    
    /**
     * Décode un token de confirmation d'email pour retrouver l'adresse email
     */
    private String decodeEmailFromToken(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            // Le format du token est email:uuid
            return decoded.split(":")[0];
        } catch (Exception e) {
            throw new RuntimeException("Token invalide");
        }
    }
}