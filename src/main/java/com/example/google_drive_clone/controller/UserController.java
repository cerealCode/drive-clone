package com.example.google_drive_clone.controller;

import com.example.google_drive_clone.model.User;
import com.example.google_drive_clone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            // Log incoming user data
            System.out.println("Registering user: " + user.getUsername());

            // Save or update the user
            userService.saveOrUpdateUser(user);

            // Log successful registration
            System.out.println("User registered successfully: " + user.getUsername());

            return ResponseEntity.ok("Registration successful!");
        } catch (Exception e) {
            // Log the exception details
            System.err.println("Error during user registration: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }
}
