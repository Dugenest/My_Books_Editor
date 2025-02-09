package com.afci.service;

import com.afci.data.Administrator;
import com.afci.data.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdministratorService {

    @Autowired
    private AdministratorRepository administratorRepository;

    public List<Administrator> getAllAdministrators() {
        return administratorRepository.findAll();
    }

    public Optional<Administrator> getAdministratorById(Long id) {
        return administratorRepository.findById(id);
    }

    public Administrator createAdministrator(Administrator administrator) {
        return administratorRepository.save(administrator);
    }

    public Administrator updateAdministrator(Administrator administrator) {
        if (administratorRepository.existsById(administrator.getId())) {
            return administratorRepository.save(administrator);
        }
        throw new RuntimeException("Administrateur non trouvé avec l'ID : " + administrator.getId());
    }

    public void deleteAdministrator(Long id) {
        administratorRepository.deleteById(id);
    }

    public Optional<Administrator> findByEmail(String email) {
        return administratorRepository.findByEmail(email);
    }

    public List<Administrator> findByRole(String role) {
        return administratorRepository.findByRole(role);
    }
}