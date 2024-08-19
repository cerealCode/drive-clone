package com.example.google_drive_clone.controller;


import com.example.google_drive_clone.model.File;
import com.example.google_drive_clone.model.User;
import com.example.google_drive_clone.service.FileService;
import com.example.google_drive_clone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @PostMapping("/upload")
    public File uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("username") String username) throws IOException {
        User user = userService.findByUsername(username);

        File newFile = new File();
        newFile.setFilename(file.getOriginalFilename());
        newFile.setFileType(file.getContentType());
        newFile.setData(file.getBytes());
        newFile.setUploadTime(LocalDateTime.now());
        newFile.setOwner(user);

        return fileService.save(newFile);
    }

    @GetMapping("/user/{username}")
    public List<File> getFilesByUser(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return fileService.findFilesByOwner(user.getId());
    }

    @GetMapping("/{fileId}")
    public File getFileById(@PathVariable Long fileId) {
        return fileService.findById(fileId);
    }
}
