package com.afci.service;

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

import com.afci.data.Serial;
import com.afci.repository.SerialRepository;

@ExtendWith(MockitoExtension.class)
public class SerialServiceTest {

    @Mock
    private SerialRepository serialRepository;

    @InjectMocks
    private SerialService serialService;

    private Serial serial;

    @BeforeEach
    void setUp() {
        serial = new Serial();
        serial.setId(1L);
        serial.setTitle("Test Serial");
    }

    @Test
    void getAllSerials_ShouldReturnAllSerials() {
        when(serialRepository.findAll()).thenReturn(Arrays.asList(serial));

        List<Serial> result = serialService.getAllSerials();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Serial", result.get(0).getTitle());
    }

    @Test
    void getSerialById_ShouldReturnSerial() {
        when(serialRepository.findById(1L)).thenReturn(Optional.of(serial));

        Optional<Serial> result = serialService.getSerialById(1L);

        assertTrue(result.isPresent());
        assertEquals(serial.getId(), result.get().getId());
    }

    @Test
    void createSerial_ShouldReturnSavedSerial() {
        when(serialRepository.save(any(Serial.class))).thenReturn(serial);

        Serial result = serialService.createSerial(serial);

        assertNotNull(result);
        assertEquals(serial.getTitle(), result.getTitle());
    }

    @Test
    void updateSerial_WhenExists_ShouldReturnUpdatedSerial() {
        when(serialRepository.existsById(1L)).thenReturn(true);
        when(serialRepository.save(any(Serial.class))).thenReturn(serial);

        Serial result = serialService.updateSerial(serial);

        assertNotNull(result);
        assertEquals(serial.getTitle(), result.getTitle());
    }

    @Test
    void updateSerial_WhenNotExists_ShouldThrowException() {
        when(serialRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> serialService.updateSerial(serial));
    }

    @Test
    void deleteSerial_ShouldCallRepositoryDelete() {
        doNothing().when(serialRepository).deleteById(1L);

        serialService.deleteSerial(1L);

        verify(serialRepository).deleteById(1L);
    }

    @Test
    void findByTitle_ShouldReturnMatchingSerials() {
        when(serialRepository.findByTitleContainingIgnoreCase("Test"))
            .thenReturn(Arrays.asList(serial));

        List<Serial> result = serialService.findByTitle("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getTitle().contains("Test"));
    }

    @Test
    void findByAuthorId_ShouldReturnAuthorSerials() {
        when(serialRepository.findByAuthor_Id(1L)).thenReturn(Arrays.asList(serial));

        List<Serial> result = serialService.findByAuthorId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
} 