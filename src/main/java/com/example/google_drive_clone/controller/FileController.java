package com.example.google_drive_clone.controller;

import com.example.google_drive_clone.model.File;
import com.example.google_drive_clone.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            fileService.saveFile(file, username);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            System.err.println("Error in uploadFile: " + e.getMessage());
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<File>> listUserFiles() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            List<File> files = fileService.getUserFiles(username);
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            System.err.println("Error in listUserFiles: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
