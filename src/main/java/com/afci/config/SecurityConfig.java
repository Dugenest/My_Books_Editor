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
                .requestMatchers("/api/books/**", "/api/authors/**", "/api/categories/**", 
                                "/api/popular/**", "/api/new-releases/**", "/api/recommendations/**").permitAll()
                // Endpoints protégés - accessible uniquement aux admins
                // Temporairement changer à authenticated() pour tester
                .requestMatchers("/api/dashboard/**", "/dashboard/**", "/api/dashboard/stats/**", "/api/activities/**")
                .authenticated()
                // Endpoints utilisateurs authentifiés
                .requestMatchers("/api/users/**", "/api/baskets/**", "/api/comments/**", "/api/orders/**").authenticated()
                .anyRequest().authenticated()
            )
            // Ajoutez cette ligne pour ajouter le filtre JWT
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            
        System.out.println("Configuration de sécurité initialisée avec succès");
        return http.build();
    }
}