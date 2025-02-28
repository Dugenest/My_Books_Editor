package com.afci.controller;

import com.afci.data.Administrator;
import com.afci.service.AdministratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdministratorControllerTest {

    @InjectMocks
    private AdministratorController administratorController;

    @Mock
    private AdministratorService administratorService;

    private Administrator administrator;

    @BeforeEach
    void setUp() {
        // Initialisation des mocks
        MockitoAnnotations.openMocks(this);

        // Création d'un administrateur pour les tests
        administrator = new Administrator();
        administrator.setId(1L);
        administrator.setEmail("admin@domain.com");
        administrator.setRole("ROLE_ADMIN");
    }

    @SuppressWarnings({ "null", "deprecation" })
	@Test
    void testGetAllAdministrators() {
        // Mock de la réponse du service
        when(administratorService.getAllAdministrators()).thenReturn(List.of(administrator));

        // Appel de la méthode du contrôleur
        ResponseEntity<List<Administrator>> response = administratorController.getAllAdministrators();

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue()); // Vérifie le code HTTP
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size()); // Vérifie le nombre d'administrateurs
        assertEquals(administrator, response.getBody().get(0)); // Vérifie l'administrateur
    }

    @SuppressWarnings("deprecation")
	@Test
    void testCreateAdministrator() {
        // Mock de la création d'un administrateur
        when(administratorService.createAdministrator(administrator)).thenReturn(administrator);

        // Appel de la méthode du contrôleur
        ResponseEntity<Administrator> response = administratorController.createAdministrator(administrator);

        // Assertions
        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue()); // Vérifie le code HTTP
        assertEquals(administrator, response.getBody()); // Vérifie que le bon administrateur est retourné
    }

    @SuppressWarnings("deprecation")
	@Test
    void testGetAdministratorById() {
        // Mock de la recherche par ID
        when(administratorService.getAdministratorById(1L)).thenReturn(Optional.of(administrator));

        // Appel de la méthode du contrôleur
        ResponseEntity<Administrator> response = administratorController.getAdministratorById(1L);

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue()); // Vérifie le code HTTP

        // Vérifie que l'administrateur retourné est bien celui attendu
        assertNotNull(response.getBody());
        assertEquals(administrator, response.getBody()); // Compare l'administrateur retourné
    }



    @SuppressWarnings("deprecation")
	@Test
    void testDeleteAdministrator() {
        // Mock de l'existence de l'administrateur
        when(administratorService.existsById(1L)).thenReturn(true);
        doNothing().when(administratorService).deleteAdministrator(1L);

        // Appel de la méthode du contrôleur
        ResponseEntity<Void> response = administratorController.deleteAdministrator(1L);

        // Assertions
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue()); // Vérifie le code HTTP
        verify(administratorService, times(1)).deleteAdministrator(1L); // Vérifie que deleteAdministrator a bien été appelé
    }

    @SuppressWarnings("deprecation")
	@Test
    void testUpdateAdministrator() {
        // Mock de la mise à jour de l'administrateur
        when(administratorService.updateAdministrator(administrator)).thenReturn(administrator);

        // Appel de la méthode du contrôleur
        ResponseEntity<Administrator> response = administratorController.updateAdministrator(null, administrator);

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue()); // Vérifie le code HTTP
        assertEquals(administrator, response.getBody()); // Vérifie l'administrateur mis à jour
    }
}
