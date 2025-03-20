package com.afci.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.afci.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Ajoutez cette ligne pour injecter le filtre JWT
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
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
                // Ajoutez cette ligne pour ajouter le filtre JWT
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        System.out.println("Configuration de sécurité initialisée avec succès");
        return http.build();
    }
}