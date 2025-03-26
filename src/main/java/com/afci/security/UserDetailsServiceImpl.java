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
        
        // Création d'une seule autorité à partir du rôle unique
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRole() != null && !user.getRole().isEmpty()) {
            // Ajouter le préfixe ROLE_ si nécessaire
            String roleWithPrefix = user.getRole().startsWith("ROLE_") 
                ? user.getRole() 
                : "ROLE_" + user.getRole();
            authorities.add(new SimpleGrantedAuthority(roleWithPrefix));
        }
        
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // Utiliser l'email comme identifiant
                user.getPassword(),
                user.isActive(),
                true, 
                true, 
                true, 
                authorities);
    }
}