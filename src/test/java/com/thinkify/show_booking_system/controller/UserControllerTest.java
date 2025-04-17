package com.thinkify.show_booking_system.controller;

import com.thinkify.show_booking_system.model.Role;
import com.thinkify.show_booking_system.model.User;
import com.thinkify.show_booking_system.pojo.LoginRequest;
import com.thinkify.show_booking_system.pojo.LoginResponse;
import com.thinkify.show_booking_system.pojo.UserResponse;
import com.thinkify.show_booking_system.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User(1L, "test@example.com", "password", Role.CUSTOMER);
    }

    @Test
    void testCreateUser() {
        when(userService.createUser("test@example.com", "password", "CUSTOMER")).thenReturn(mockUser);
        User createdUser = userController.createUser("test@example.com", "password", "CUSTOMER");
        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        verify(userService, times(1)).createUser("test@example.com", "password", "CUSTOMER");
    }

    @Test
    void testGetAllUsers() {
        List<UserResponse> userResponses = List.of(new UserResponse(1L, "test@example.com", "password", "CUSTOMER"));
        when(userService.getAllUsers()).thenReturn(userResponses);
        List<UserResponse> users = userController.getAllUsers();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("test@example.com", users.get(0).getEmail());
    }

    @Test
    void testGetUserById() {
        when(userService.getUserById(1L)).thenReturn(mockUser);
        User user = userController.getUserById(1L);
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testLogin() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");
        String token = "jwt-token";
        when(userService.login("test@example.com", "password")).thenReturn(token);
        ResponseEntity<LoginResponse> response = userController.login(loginRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(token, response.getBody().getToken());
    }
}