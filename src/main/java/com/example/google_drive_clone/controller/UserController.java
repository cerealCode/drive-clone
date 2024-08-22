package com.example.google_drive_clone.controller;

import com.example.google_drive_clone.model.User;
import com.example.google_drive_clone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Add this line to inject PasswordEncoder

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        userService.saveOrUpdateUser(user);
        return "Registration successful!";
    }

    @PostMapping("/login")
    public User loginUser(@RequestBody User user) {
        User foundUser = userService.findByUsername(user.getUsername());
        if (foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            return foundUser;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}
