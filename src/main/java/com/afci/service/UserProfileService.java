package com.afci.service;

import com.afci.data.UserProfile;
import com.afci.data.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public List<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    public Optional<UserProfile> getUserProfileById(Long id) {
        return userProfileRepository.findById(id);
    }

    public UserProfile createUserProfile(Long userId) {
        // L'utilisateur est peut-être créé par un autre service,
        // ici on créerait un profil associé
        UserProfile userProfile = new UserProfile();
        userProfile.setId_profile(userId);
        return userProfileRepository.save(userProfile);
    }

    public UserProfile updateUserProfile(UserProfile userProfile) {
        if (userProfileRepository.existsById(userProfile.getId_profile())) {
            return userProfileRepository.save(userProfile);
        }
        throw new RuntimeException("UserProfile non trouvé avec l'ID : " + userProfile.getId_profile());
    }

    public void deleteUserProfile(Long id) {
        userProfileRepository.deleteById(id);
    }

    public UserProfile findByEmail(String email) {
        return userProfileRepository.findByEmail(email);
    }

    public UserProfile updateProfilePicture(Long userId, MultipartFile picture) {
        Optional<UserProfile> optionalProfile = userProfileRepository.findById(userId);
        if (optionalProfile.isPresent()) {
            UserProfile userProfile = optionalProfile.get();
            try {
                // Traitement de la photo (par exemple, stockage sur le serveur)
                // Ici, juste un exemple de mise à jour
                userProfile.setProfilePicture(picture.getBytes());
                return userProfileRepository.save(userProfile);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors du téléchargement de l'image.", e);
            }
        } else {
            throw new RuntimeException("Utilisateur non trouvé avec ID: " + userId);
        }
    }
}
