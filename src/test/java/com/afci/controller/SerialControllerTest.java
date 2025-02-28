package com.afci.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.afci.data.Serial;
import com.afci.service.SerialService;

@ExtendWith(MockitoExtension.class)
public class SerialControllerTest {

    @Mock
    private SerialService serialService;

    @InjectMocks
    private SerialController serialController;

    private Serial serial;

    @BeforeEach
    void setUp() {
        serial = new Serial();
        serial.setId(1L);
        serial.setTitle("Test Serial");
    }

    @Test
    void getAllSerials_ShouldReturnSerials() {
        List<Serial> serials = Arrays.asList(serial);
        when(serialService.getAllSerials()).thenReturn(serials);

        ResponseEntity<List<Serial>> response = serialController.getAllSerials();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getSerialById_ShouldReturnSerial() {
        when(serialService.getSerialById(1L)).thenReturn(Optional.of(serial));

        ResponseEntity<Optional<Serial>> response = serialController.getSerialById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
    }

    @Test
    void getSerialBooks_ShouldReturnSerialWithBooks() {
        when(serialService.getSerialById(1L)).thenReturn(Optional.of(serial));

        ResponseEntity<Optional<Serial>> response = serialController.getSerialBooks(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
    }

    @Test
    void createSerial_ShouldReturnCreatedSerial() {
        when(serialService.createSerial(any(Serial.class))).thenReturn(serial);

        ResponseEntity<Serial> response = serialController.createSerial(serial);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateSerial_ShouldReturnUpdatedSerial() {
        when(serialService.updateSerial(any(Serial.class))).thenReturn(serial);

        ResponseEntity<Serial> response = serialController.updateSerial(1L, serial);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deleteSerial_ShouldReturnNoContent() {
        doNothing().when(serialService).deleteSerial(1L);

        ResponseEntity<Void> response = serialController.deleteSerial(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
} 