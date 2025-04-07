package com.afci.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afci.data.User;
import com.afci.repository.UserRepository;

public class AvatarMigrationServic {
    @Service
    public class AvatarMigrationService {

        @Autowired
        private UserRepository userRepository;

        @Transactional
        public void standardizeAvatarPaths() {
            Iterable<User> users = userRepository.findAll();
            int updatedCount = 0;

            for (User user : users) {
                String avatarPath = user.getAvatar();
                boolean updated = false;

                if (avatarPath == null) {
                    user.setAvatar("/api/files/default-avatar");
                    updated = true;
                } else if (avatarPath.equals("assets/default-avatar.png")) {
                    user.setAvatar("/api/files/default-avatar");
                    updated = true;
                } else if (avatarPath.startsWith("avatars/")) {
                    String filename = avatarPath.substring("avatars/".length());
                    user.setAvatar("/api/files/avatars/" + filename);
                    updated = true;
                } else if (!avatarPath.startsWith("/api/")) {
                    // Si le chemin n'est pas au format API et n'est pas traité ci-dessus
                    // On suppose qu'il s'agit d'un nom de fichier simple
                    user.setAvatar("/api/files/avatars/" + avatarPath);
                    updated = true;
                }

                if (updated) {
                    userRepository.save(user);
                    updatedCount++;
                }
            }

            System.out.println("Standardized avatar paths for " + updatedCount + " users");
        }
    }
}
