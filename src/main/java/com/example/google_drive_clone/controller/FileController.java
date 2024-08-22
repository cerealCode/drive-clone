// FileController.java

package com.example.google_drive_clone.controller;

import com.example.google_drive_clone.model.File;
import com.example.google_drive_clone.model.User;
import com.example.google_drive_clone.service.FileService;
import com.example.google_drive_clone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    private static final String UPLOADED_FOLDER = "C://temp//"; // Update this path accordingly

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            File newFile = new File();
            newFile.setFileName(file.getOriginalFilename());
            newFile.setFilePath(path.toString());
            newFile.setUser(user);
            newFile.setUploadTime(LocalDateTime.now());

            fileService.save(newFile);

            model.addAttribute("message", "File uploaded successfully!");
        } catch (IOException e) {
            model.addAttribute("message", "Failed to upload file: " + e.getMessage());
        }

        return "uploadStatus"; // Adjust this return value to your actual view name
    }
}
