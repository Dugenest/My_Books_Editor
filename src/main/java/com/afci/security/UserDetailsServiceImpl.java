package com.afci.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afci.data.User;
import com.afci.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email));
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // Récupérer le rôle de l'utilisateur depuis la base de données
        String role = user.getRole();
        if (role != null && !role.isEmpty()) {
            // Ajouter le préfixe ROLE_ si nécessaire
            String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role;
            authorities.add(new SimpleGrantedAuthority(roleWithPrefix));
            
            // Ajouter les rôles supplémentaires selon le type d'utilisateur
            if (role.equals("ADMIN")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_AUTHOR"));
                authorities.add(new SimpleGrantedAuthority("ROLE_EDITOR"));
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            } else if (role.equals("AUTHOR")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            } else if (role.equals("EDITOR")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
        }
        
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isActive(),
                true, 
                true, 
                true, 
                authorities);
    }
}