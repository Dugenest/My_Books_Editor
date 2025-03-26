package com.afci.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.afci.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        // Endpoints publics
                        .requestMatchers(HttpMethod.GET, "/api/books/**", "/api/authors/**", "/api/categories/**",
                                "/api/popular/**", "/api/new-releases/**", "/api/recommendations/**")
                        .permitAll()
                        // Permettre toutes les opérations sur les auteurs sans authentification (pour
                        // le test)
                        .requestMatchers(HttpMethod.PUT, "/api/authors/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/authors/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/authors/**").permitAll()
                        // Autoriser explicitement les nouveaux endpoints
                        .requestMatchers("/api/authors/*/simple-update").permitAll()
                        .requestMatchers("/api/authors/*/update-bypass").permitAll()
                        // Endpoints de suppression forcée - accessible à tous pour le test
                        .requestMatchers("/api/users/force-delete-by-author/**", "/api/users/by-author/**",
                                "/api/authors/force-delete/**")
                        .permitAll()
                        // Endpoints du tableau de bord - accessible à tous pour le test
                        .requestMatchers("/api/dashboard/**", "/dashboard/**", "/api/dashboard/stats/**",
                                "/api/activities/**")
                        .permitAll()
                        // Endpoints utilisateurs authentifiés
                        .requestMatchers("/api/users/**", "/api/baskets/**", "/api/comments/**", "/api/orders/**")
                        .permitAll()
                        .anyRequest().permitAll())
                // Activer l'authentification par formulaire avec redirection personnalisée
                .formLogin(form -> form
                        .disable())
                // Définir la gestion de la déconnexion
                .logout(logout -> logout
                        .disable())
                // Ajouter le filtre JWT
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        System.out.println("Configuration de sécurité initialisée avec succès");
        return http.build();
    }
}