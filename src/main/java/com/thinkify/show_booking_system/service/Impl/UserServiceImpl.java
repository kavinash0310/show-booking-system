package com.thinkify.show_booking_system.service.Impl;

import com.thinkify.show_booking_system.constant.Constants;
import com.thinkify.show_booking_system.exception.UserNotFoundException;
import com.thinkify.show_booking_system.model.Role;
import com.thinkify.show_booking_system.model.User;
import com.thinkify.show_booking_system.pojo.UserResponse;
import com.thinkify.show_booking_system.repository.UserRepository;
import com.thinkify.show_booking_system.security.JwtUtil;
import com.thinkify.show_booking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.thinkify.show_booking_system.constant.Constants.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil=jwtUtil;
    }

    public User createUser(String email, String password, String roleName) {
        Role role = Role.valueOf(roleName);
        User user= new User(null, email, password, role);
        return userRepository.save(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getRole().name()
                ))
                .toList();
    }

    public String login(String email, String password) {
        User dbUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(INVALID_EMAIL));

        if (!password.equals(dbUser.getPassword())) {
            throw new RuntimeException(INVALID_PASSWORD);
        }

        return jwtUtil.generateToken(dbUser.getEmail(), dbUser.getRole());
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format(Constants.USER_NOT_FOUND, userId)));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format(Constants.USER_NOT_FOUND, email)));
    }
}