// package com.afci.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;

// @Configuration
// public class SecurityPasswordConfig {

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         // Le paramètre 12 représente la force du hachage (entre 4 et 31)
//         // Plus le nombre est élevé, plus le hachage est sécurisé mais lent
//         return new BCryptPasswordEncoder(12);
//     }
    
//     // Optionnel : Méthode utilitaire pour vérifier la force du mot de passe
//     public boolean isPasswordStrong(String password) {
//         // Au moins 8 caractères
//         if (password.length() < 8) return false;
        
//         // Au moins une lettre majuscule
//         if (!password.matches(".*[A-Z].*")) return false;
        
//         // Au moins une lettre minuscule
//         if (!password.matches(".*[a-z].*")) return false;
        
//         // Au moins un chiffre
//         if (!password.matches(".*\\d.*")) return false;
        
//         // Au moins un caractère spécial
//         if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) return false;
        
//         return true;
//     }
// }