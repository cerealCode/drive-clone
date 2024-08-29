package com.example.google_drive_clone.service;

import com.example.google_drive_clone.model.User;
import com.example.google_drive_clone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FolderService folderService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, FolderService folderService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.folderService = folderService;
    }

    @Transactional
    public User register(User user) throws Exception {
        logger.info("Registering user: {}", user.getUsername());

        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            logger.error("Username {} already exists", user.getUsername());
            throw new Exception("Username already exists");
        }

        // Encode the password before saving the user
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) { // Ensure password isn't already encoded
            logger.info("Encoding password for user: {}", user.getUsername());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // Save the user
        User savedUser = userRepository.save(user);
        logger.info("User registered with ID: {}", savedUser.getId());

        // Create default folder for the user
        try {
            folderService.createFolder("Default Folder", savedUser.getUsername());
        } catch (Exception e) {
            logger.error("Failed to create default folder for user: {}", savedUser.getUsername(), e);
            throw new Exception("Registration failed: Unable to create default folder");
        }

        return savedUser;
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
