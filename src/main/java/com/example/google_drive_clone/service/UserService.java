package com.example.google_drive_clone.service;

import com.example.google_drive_clone.model.User;
import com.example.google_drive_clone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User saveOrUpdateUser(User user) {
        try {
            System.out.println("Saving user: " + user.getUsername());
            
            // Password encoding
            if (user.getPassword() != null && !user.getPassword().startsWith("{bcrypt}")) {
                System.out.println("Encoding password for user: " + user.getUsername());
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            User savedUser = userRepository.save(user);
            System.out.println("User saved with ID: " + savedUser.getId());

            return savedUser;
        } catch (Exception e) {
            System.err.println("Error saving user: " + e.getMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Page<User> findAllUsersWithPagination(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
