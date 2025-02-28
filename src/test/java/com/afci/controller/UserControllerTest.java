package com.afci.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.afci.data.PasswordChangeRequest;
import com.afci.data.User;
import com.afci.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;
    private PasswordChangeRequest passwordRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        passwordRequest = new PasswordChangeRequest();
        passwordRequest.setOldPassword("oldPassword");
        passwordRequest.setNewPassword("newPassword");
    }

    @Test
    void getAllUsers_ShouldReturnUsers() {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user));

        ResponseEntity<Object> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getUserById_ShouldReturnUser() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<Object> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        when(userService.createUser(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);

        ResponseEntity<Object> response = userController.updateUser(1L, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deleteUser_ShouldReturnNoContent() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void changePassword_WhenSuccess_ShouldReturnOk() {
        doNothing().when(userService).changePassword(eq(1L), any(PasswordChangeRequest.class));

        ResponseEntity<Void> response = userController.changePassword(1L, passwordRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void changePassword_WhenError_ShouldThrowException() {
        doThrow(new RuntimeException()).when(userService)
            .changePassword(eq(1L), any(PasswordChangeRequest.class));

        assertThrows(RuntimeException.class, () -> userController.changePassword(1L, passwordRequest));
    }
} 