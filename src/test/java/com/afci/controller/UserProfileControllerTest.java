package com.afci.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.afci.data.UserProfile;
import com.afci.service.UserProfileService;

@ExtendWith(MockitoExtension.class)
public class UserProfileControllerTest {

    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private UserProfileController userProfileController;

    private UserProfile userProfile;
    private MockMultipartFile picture;

    @BeforeEach
    void setUp() {
        userProfile = new UserProfile();
        userProfile.setId_profile(1L);
        userProfile.setEmail("test@example.com");

        picture = new MockMultipartFile(
            "file",
            "test.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );
    }

    @Test
    void getUserProfile_WhenExists_ShouldReturnProfile() {
        when(userProfileService.getUserProfileById(1L)).thenReturn(Optional.of(userProfile));

        ResponseEntity<UserProfile> response = userProfileController.getUserProfile(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getUserProfile_WhenNotExists_ShouldReturnNotFound() {
        when(userProfileService.getUserProfileById(1L)).thenReturn(Optional.empty());

        ResponseEntity<UserProfile> response = userProfileController.getUserProfile(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateUserProfile_WhenSuccess_ShouldReturnUpdatedProfile() {
        when(userProfileService.updateUserProfile(any(UserProfile.class))).thenReturn(userProfile);

        ResponseEntity<UserProfile> response = userProfileController.updateUserProfile(1L, userProfile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateUserProfile_WhenError_ShouldReturnNotFound() {
        when(userProfileService.updateUserProfile(any(UserProfile.class)))
            .thenThrow(new RuntimeException());

        ResponseEntity<UserProfile> response = userProfileController.updateUserProfile(1L, userProfile);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateProfilePicture_WhenSuccess_ShouldReturnUpdatedProfile() {
        when(userProfileService.updateProfilePicture(eq(1L), any(MockMultipartFile.class)))
            .thenReturn(userProfile);

        ResponseEntity<UserProfile> response = 
            userProfileController.updateProfilePicture(1L, picture);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateProfilePicture_WhenError_ShouldReturnNotFound() {
        when(userProfileService.updateProfilePicture(eq(1L), any(MockMultipartFile.class)))
            .thenThrow(new RuntimeException());

        ResponseEntity<UserProfile> response = 
            userProfileController.updateProfilePicture(1L, picture);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteUserProfile_WhenSuccess_ShouldReturnOk() {
        doNothing().when(userProfileService).deleteUserProfile(1L);

        ResponseEntity<Void> response = userProfileController.deleteUserProfile(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteUserProfile_WhenError_ShouldReturnNotFound() {
        doThrow(new RuntimeException()).when(userProfileService).deleteUserProfile(1L);

        ResponseEntity<Void> response = userProfileController.deleteUserProfile(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
} 