package com.thinkify.show_booking_system.service;

import com.thinkify.show_booking_system.exception.UserNotFoundException;
import com.thinkify.show_booking_system.model.Role;
import com.thinkify.show_booking_system.model.User;
import com.thinkify.show_booking_system.repository.UserRepository;
import com.thinkify.show_booking_system.security.JwtUtil;
import com.thinkify.show_booking_system.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User(1L, "test@example.com", "password", Role.CUSTOMER);
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        User createdUser = userService.createUser("test@example.com", "password", "CUSTOMER");
        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(mockUser));
        var users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("test@example.com", users.get(0).getEmail());
    }

    @Test
    void testLogin_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(jwtUtil.generateToken("test@example.com", Role.CUSTOMER)).thenReturn("jwt-token");
        String token = userService.login("test@example.com", "password");
        assertEquals("jwt-token", token);
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> {
            userService.login("test@example.com", "password");
        });
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        User user = userService.getUserById(1L);
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(1L);
        });
    }
}
