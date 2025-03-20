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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.afci.data.PasswordChangeRequest;
import com.afci.data.User;
import com.afci.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

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

        lenient().when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        lenient().when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
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
        User existingUser = new User("oldUsername", "oldPassword", "old@email.com");
        existingUser.setId(1L);
        existingUser.setFirstName("OldFirst");
        existingUser.setLastName("OldLast");
        existingUser.setPhone("0123456789");
        existingUser.setAddress("Old Address");
        existingUser.setRole("USER");
        existingUser.setActive(true);
        existingUser.setSubscribedToNewsletter(false);
        existingUser.setBooks(new HashSet<>());
        existingUser.setOrders(new HashSet<>());
        
        User updatedUser = new User("newUsername", "newPassword", "new@email.com");
        updatedUser.setId(1L);
        updatedUser.setFirstName("NewFirst");
        updatedUser.setLastName("NewLast");
        updatedUser.setPhone("9876543210");
        updatedUser.setAddress("New Address");
        updatedUser.setRole("USER");
        updatedUser.setActive(true);
        updatedUser.setSubscribedToNewsletter(true);
        updatedUser.setBooks(new HashSet<>());
        updatedUser.setOrders(new HashSet<>());
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        assertEquals("newUsername", result.getUsername());
        assertEquals("new@email.com", result.getEmail());
        assertEquals("NewFirst", result.getFirstName());
        assertEquals("NewLast", result.getLastName());
    }

    @Test
    void updateUser_WhenNotExists_ShouldThrowException() {
        User updatedUser = new User();
        updatedUser.setId(1L);
        lenient().when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(1L, updatedUser));
    }

    @Test
    void deleteUser_WhenExists_ShouldDelete() {
        User existingUser = new User("testUser", "password", "test@email.com");
        existingUser.setId(1L);
        existingUser.setFirstName("Test");
        existingUser.setLastName("User");
        existingUser.setPhone("0123456789");
        existingUser.setAddress("Test Address");
        existingUser.setRole("USER");
        existingUser.setActive(true);
        existingUser.setSubscribedToNewsletter(false);
        existingUser.setBooks(new HashSet<>());
        existingUser.setOrders(new HashSet<>());
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        assertDoesNotThrow(() -> userService.deleteUser(1L));
    }

    @Test
    void deleteUser_WhenNotExists_ShouldThrowException() {
        lenient().when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
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
        lenient().when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.changePassword(1L, passwordRequest));
    }
} 