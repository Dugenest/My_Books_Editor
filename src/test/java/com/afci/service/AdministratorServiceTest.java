package com.afci.service;

import com.afci.data.Administrator;
import com.afci.data.AdministratorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

class AdministratorServiceTest {

    @InjectMocks
    private AdministratorService administratorService;

    @Mock
    private AdministratorRepository administratorRepository;

    private Administrator administrator;

    @BeforeEach
    void setUp() {
        // Initialisation des mocks
        MockitoAnnotations.openMocks(this);

        // Création d'un administrateur pour les tests
        administrator = new Administrator();
        administrator.setId(1L);  // Ajoute un ID valide
        administrator.setEmail("admin@domain.com");
        administrator.setRole("ROLE_ADMIN");
    }

    @Test
    void testGetAllAdministrators() {
        when(administratorRepository.findAll()).thenReturn(List.of(administrator));

        List<Administrator> administrators = administratorService.getAllAdministrators();

        assertNotNull(administrators);
        assertFalse(administrators.isEmpty());
        assertEquals(1, administrators.size());
        assertEquals(administrator, administrators.get(0));

        verify(administratorRepository, times(1)).findAll();
    }

    @Test
    void testFindByRole() {
        String role = "ROLE_ADMIN";
        when(administratorRepository.findByRole(role)).thenReturn(List.of(administrator));

        List<Administrator> administrators = administratorService.findByRole(role);

        assertNotNull(administrators);
        assertFalse(administrators.isEmpty());
        assertEquals(1, administrators.size());

        verify(administratorRepository, times(1)).findByRole(role);
    }

    @Test
    void testUpdateAdministrator() {
        // Lorsque l'administrateur existe dans la base de données
        when(administratorRepository.existsById(1L)).thenReturn(true);
        when(administratorRepository.save(administrator)).thenReturn(administrator);

        // Appel à la méthode de mise à jour
        Administrator updatedAdministrator = administratorService.updateAdministrator(administrator);

        // Vérifie que l'administrateur mis à jour est celui que nous avons passé
        assertNotNull(updatedAdministrator);
        assertEquals(1L, updatedAdministrator.getId());
        assertEquals("admin@domain.com", updatedAdministrator.getEmail());
        assertEquals("ROLE_ADMIN", updatedAdministrator.getRole());

        // Vérifie que la méthode save a été appelée exactement une fois
        verify(administratorRepository, times(1)).save(administrator);
    }

    @Test
    void testCreateAdministrator() {
        when(administratorRepository.save(administrator)).thenReturn(administrator);

        Administrator createdAdministrator = administratorService.createAdministrator(administrator);

        assertNotNull(createdAdministrator);
        assertEquals(administrator, createdAdministrator);

        verify(administratorRepository, times(1)).save(administrator);
    }

    @Test
    void testUpdateAdministratorNotFound() {
        // Lorsque l'administrateur n'existe pas dans la base de données
        when(administratorRepository.existsById(1L)).thenReturn(false);

        // Appel à la méthode de mise à jour et vérifie que l'exception est levée
        assertThrows(RuntimeException.class, () -> administratorService.updateAdministrator(administrator));
    }

    @Test
    void testGetAdministratorById() {
        when(administratorRepository.findById(1L)).thenReturn(Optional.of(administrator));

        Optional<Administrator> foundAdministrator = administratorService.getAdministratorById(1L);

        assertTrue(foundAdministrator.isPresent());
        assertEquals(administrator, foundAdministrator.get());

        verify(administratorRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByEmail() {
        when(administratorRepository.findByEmail("admin@domain.com")).thenReturn(Optional.of(administrator));

        Optional<Administrator> foundAdministrator = administratorService.findByEmail("admin@domain.com");

        assertTrue(foundAdministrator.isPresent());
        assertEquals(administrator, foundAdministrator.get());

        verify(administratorRepository, times(1)).findByEmail("admin@domain.com");
    }

    @Test
    void testDeleteAdministrator() {
        // Lorsque l'administrateur existe dans la base de données
        when(administratorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(administratorRepository).deleteById(1L);

        administratorService.deleteAdministrator(1L);

        // Vérifie que la méthode deleteById a bien été appelée une fois
        verify(administratorRepository, times(1)).deleteById(1L);
    }
}
