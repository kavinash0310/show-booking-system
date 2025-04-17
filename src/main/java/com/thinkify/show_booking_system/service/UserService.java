package com.thinkify.show_booking_system.service;

import com.thinkify.show_booking_system.model.User;
import com.thinkify.show_booking_system.pojo.UserResponse;

import java.util.List;

public interface UserService {
    User createUser(String email, String password, String roleName);
    List<UserResponse> getAllUsers();
    String login(String email, String password);
    User getUserById(Long userId);
    User getUserByEmail(String email);
}
