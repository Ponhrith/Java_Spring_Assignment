package com.sunrise.assignment.controller;

import com.sunrise.assignment.model.User;
import com.sunrise.assignment.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testCreateUser() {
        User user = new User();
        when(userService.saveUser(user)).thenReturn(user);

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(200, response.getStatusCodeValue());
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    void testUpdateUser() {
        User updatedUser = new User();
        when(userService.updateUser(1L, updatedUser)).thenReturn(Optional.of(updatedUser));

        ResponseEntity<User> response = userController.updateUser(1L, updatedUser);

        assertEquals(200, response.getStatusCodeValue());
        verify(userService, times(1)).updateUser(1L, updatedUser);
    }

    @Test
    void testUpdateUser_NotFound() {
        User user = new User();
        when(userService.updateUser(eq(1L), eq(user))).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.updateUser(1L, user);

        assertEquals(404, response.getStatusCodeValue());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService, times(1)).updateUser(eq(1L), userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals(user, capturedUser); // Ensure the same object is passed
    }



    @Test
    void testDeleteUser() {
        when(userService.deleteUser(1L)).thenReturn(true);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userService.deleteUser(1L)).thenReturn(false);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(404, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(1L);
    }
}
