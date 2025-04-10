package com.afci.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.afci.data.PasswordChangeRequest;
import com.afci.data.User;
import com.afci.repository.UserRepository;
import com.afci.service.FileService;
import com.afci.data.Book;
import com.afci.data.Order;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileService fileService;

    @Mock
    private PasswordEncoder passwordEncoder;

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

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(fileService.getDefaultAvatarPath()).thenReturn("default-avatar.png");
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
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUsername");
        existingUser.setEmail("old@email.com");
        existingUser.setPassword("oldPassword");
        
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("newUsername");
        updatedUser.setEmail("new@email.com");
        updatedUser.setPassword("newPassword");
        
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        assertEquals("newUsername", result.getUsername());
        assertEquals("new@email.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_WhenNotExists_ShouldThrowException() {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("newUsername");
        updatedUser.setEmail("new@email.com");
        updatedUser.setPassword("newPassword");
        
        when(userRepository.existsById(1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(1L, updatedUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_WhenExists_ShouldDelete() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("testUser");
        existingUser.setEmail("test@email.com");
        existingUser.setPassword("testPassword");
        
        HashSet<Book> emptyBooks = new HashSet<>();
        HashSet<Order> emptyOrders = new HashSet<>();
        existingUser.setBooks(emptyBooks);
        existingUser.setOrders(emptyOrders);
        
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        doNothing().when(userRepository).deleteById(1L);
        when(fileService.getDefaultAvatarPath()).thenReturn("default-avatar.png");

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_WhenNotExists_ShouldThrowException() {
        when(userRepository.existsById(1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        verify(userRepository, never()).deleteById(1L);
    }

    @Test
    void changePassword_WhenValidOldPassword_ShouldUpdate() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertDoesNotThrow(() -> userService.changePassword(1L, passwordRequest));
    }

    @Test
    void changePassword_WhenInvalidOldPassword_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.changePassword(1L, passwordRequest));
    }
} 