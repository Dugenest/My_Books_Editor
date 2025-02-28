package com.afci.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.afci.data.UserProfile;
import com.afci.data.UserProfileRepository;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    private UserProfile userProfile;
    private MockMultipartFile picture;

    @BeforeEach
    void setUp() {
        userProfile = new UserProfile();
        userProfile.setId_profile(1L);
        userProfile.setEmail("test@example.com");

        picture = new MockMultipartFile(
            "picture",
            "test.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );
    }

    @Test
    void getAllUserProfiles_ShouldReturnAllProfiles() {
        when(userProfileRepository.findAll()).thenReturn(Arrays.asList(userProfile));

        List<UserProfile> result = userProfileService.getAllUserProfiles();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getUserProfileById_ShouldReturnProfile() {
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(userProfile));

        Optional<UserProfile> result = userProfileService.getUserProfileById(1L);

        assertTrue(result.isPresent());
        assertEquals(userProfile.getId_profile(), result.get().getId_profile());
    }

    @Test
    void createUserProfile_ShouldReturnNewProfile() {
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        UserProfile result = userProfileService.createUserProfile(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId_profile());
    }

    @Test
    void updateUserProfile_WhenExists_ShouldReturnUpdatedProfile() {
        when(userProfileRepository.existsById(1L)).thenReturn(true);
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        UserProfile result = userProfileService.updateUserProfile(userProfile);

        assertNotNull(result);
        assertEquals(userProfile.getId_profile(), result.getId_profile());
    }

    @Test
    void updateUserProfile_WhenNotExists_ShouldThrowException() {
        when(userProfileRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userProfileService.updateUserProfile(userProfile));
    }

    @Test
    void deleteUserProfile_ShouldCallRepositoryDelete() {
        doNothing().when(userProfileRepository).deleteById(1L);

        userProfileService.deleteUserProfile(1L);

        verify(userProfileRepository).deleteById(1L);
    }

    @Test
    void findByEmail_ShouldReturnProfile() {
        when(userProfileRepository.findByEmail("test@example.com")).thenReturn(userProfile);

        UserProfile result = userProfileService.findByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void updateProfilePicture_WhenExists_ShouldUpdatePicture() throws IOException {
        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        UserProfile result = userProfileService.updateProfilePicture(1L, picture);

        assertNotNull(result);
        assertArrayEquals(picture.getBytes(), result.getProfilePicture());
    }

    @Test
    void updateProfilePicture_WhenNotExists_ShouldThrowException() {
        when(userProfileRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, 
            () -> userProfileService.updateProfilePicture(1L, picture));
    }
} 