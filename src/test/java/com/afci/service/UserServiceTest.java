package com.afci.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.afci.data.PasswordChangeRequest;
import com.afci.data.User;
import com.afci.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private PasswordChangeRequest passwordRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setPassword("oldPassword");

        passwordRequest = new PasswordChangeRequest();
        passwordRequest.setOldPassword("oldPassword");
        passwordRequest.setNewPassword("newPassword");
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        Iterable<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, ((Iterable<User>) result).spliterator().getExactSizeIfKnown());
    }

    @Test
    void getUserById_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }

    @Test
    void updateUser_WhenExists_ShouldReturnUpdatedUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(1L, user);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }

    @Test
    void updateUser_WhenNotExists_ShouldThrowException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.updateUser(1L, user));
    }

    @Test
    void deleteUser_WhenExists_ShouldDelete() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_WhenNotExists_ShouldThrowException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void changePassword_WhenValidOldPassword_ShouldUpdate() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertDoesNotThrow(() -> userService.changePassword(1L, passwordRequest));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void changePassword_WhenInvalidOldPassword_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        passwordRequest.setOldPassword("wrongPassword");

        assertThrows(RuntimeException.class, () -> userService.changePassword(1L, passwordRequest));
    }
} 