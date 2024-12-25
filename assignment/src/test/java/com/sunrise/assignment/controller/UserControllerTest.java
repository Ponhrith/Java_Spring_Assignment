package com.sunrise.assignment.controller;

import com.sunrise.assignment.model.User;
import com.sunrise.assignment.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_shouldReturnEmptyList() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertThat(response.getBody()).isEmpty();
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void createUser_shouldReturnCreatedUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        when(userService.saveUser(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.createUser(user);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo("testUser");
        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    void updateUser_shouldUpdateAndReturnUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("updatedUser");

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.updateUser(1L, user);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo("updatedUser");
        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    @Test
    void deleteUser_shouldDeleteSuccessfully() {
        when(userService.deleteUser(1L)).thenReturn(true);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(userService, times(1)).deleteUser(1L);
    }
}
