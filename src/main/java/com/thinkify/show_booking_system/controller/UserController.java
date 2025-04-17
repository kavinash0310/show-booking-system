package com.thinkify.show_booking_system.controller;

import com.thinkify.show_booking_system.model.User;
import com.thinkify.show_booking_system.pojo.LoginRequest;
import com.thinkify.show_booking_system.pojo.LoginResponse;
import com.thinkify.show_booking_system.pojo.UserResponse;
import com.thinkify.show_booking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public User createUser(@RequestParam String email, @RequestParam String password,@RequestParam(defaultValue = "CUSTOMER") String role) {
        return userService.createUser(email, password, role.toUpperCase());
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}