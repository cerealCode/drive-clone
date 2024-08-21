package com.example.google_drive_clone.service;

import com.example.google_drive_clone.model.User;
import com.example.google_drive_clone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Method to save the user with an encoded password
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Method to retrieve all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Method to find a user by their ID
    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    // Method to find a user by their username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Method to delete a user by their ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
