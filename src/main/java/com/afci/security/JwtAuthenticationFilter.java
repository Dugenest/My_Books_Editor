package com.afci.security;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtProvider tokenProvider;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            
            if (jwt != null && tokenProvider.validateToken(jwt)) {
                // Extraire le nom d'utilisateur
                String username = tokenProvider.getUsernameFromJWT(jwt);
                
                // Charger les détails de l'utilisateur en utilisant UserDetailsService
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // Créer l'objet d'authentification avec UserDetails, pas username
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                // Définir les détails de l'authentification
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Définir l'authentification dans le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                // Débug pour vérifier les rôles
                System.out.println("Utilisateur authentifié: " + userDetails.getUsername());
                System.out.println("Rôles: " + userDetails.getAuthorities());
            }
        } catch (Exception ex) {
            System.out.println("Erreur lors de l'authentification: " + ex.getMessage());
            ex.printStackTrace(); // Pour voir la stack trace complète
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        return null;
    }
}