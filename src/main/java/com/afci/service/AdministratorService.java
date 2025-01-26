package com.afci.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.afci.data.Administrator;
import com.afci.data.AdministratorRepository;
import com.afci.exception.NotFoundException;
import jakarta.transaction.Transactional;
import com.afci.exception.DuplicateException;
import com.afci.exception.InvalidDataException;

@Service
public class AdministratorService {

    private final AdministratorRepository administratorRepository;

    public AdministratorService(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    // Obtenir un administrateur par son ID
    public Administrator getAdministratorById(Long id) {
        return administratorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Administrator not found with id: " + id));
    }

    // Vérifie si un administrateur existe par ID
    public boolean existsById(Long id) {
        return administratorRepository.existsById(id);
    }

    // Obtenir tous les administrateurs
    public List<Administrator> getAllAdministrators() {
        return (List<Administrator>) administratorRepository.findAll();
    }

    // // Créer un administrateur (vérifie avant s'il existe déjà par ID)
    // @Transactional
    // public Administrator createAdministrator(Administrator administrator) {
    //     // Vérifier si un administrateur avec le même ID existe déjà
    //     if (administrator.getAdminId() != null && administratorRepository.findById(administrator.getAdminId()).isPresent()) {
    //         throw new DuplicateException("Administrator with ID " + administrator.getAdminId() + " already exists.");
    //     }

    //     // Vérification des données invalides
    //     if (administrator.getAdminName() == null || administrator.getAdminName().isEmpty() ||
    //             administrator.getAdministratorFirstname() == null || administrator.getAdministratorFirstname().isEmpty()) {
    //         throw new InvalidDataException("Administrator name and firstname cannot be null or empty.");
    //     }

    //     // Si aucun administrateur avec cet ID n'existe, créer l'administrateur
    //     return administratorRepository.save(administrator);
    // }

    // // Update Administrator
    // @Transactional
    // public Administrator updateAdministrator(Long id, Administrator updatedAdministrator) {
    //     // Rechercher l'administrateur par ID
    //     Administrator existingAdministrator = getAdministratorById(id); // Cette méthode lève une exception si l'administrateur n'existe pas

    //     // Vérification des données invalides pour la mise à jour
    //     if (updatedAdministrator.getAdminName() == null || updatedAdministrator.getAdminName().isEmpty() ||
    //             updatedAdministrator.getAdministratorFirstname() == null || updatedAdministrator.getAdministratorFirstname().isEmpty()) {
    //         throw new InvalidDataException("Administrator name and firstname cannot be null or empty.");
    //     }

    //     // Mettre à jour les données de l'administrateur
    //     existingAdministrator.setAdminName(updatedAdministrator.getAdminName());
    //     existingAdministrator.setAdministratorFirstname(updatedAdministrator.getAdministratorFirstname());
    //     existingAdministrator.setAdministratorNationality(updatedAdministrator.getAdministratorNationality());

    //     // Sauvegarder et retourner l'administrateur mis à jour
    //     return administratorRepository.save(existingAdministrator);
    // }

    // Supprimer un administrateur
    @Transactional
    public boolean deleteAdministrator(Long id) {
        if (administratorRepository.existsById(id)) {
            administratorRepository.deleteById(id);
            return true; // Suppression réussie
        } else {
            throw new NotFoundException("Administrator not found with id: " + id);
        }
    }
}
