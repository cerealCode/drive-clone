package com.example.google_drive_clone.controller;

import java.util.Collections;
import com.example.google_drive_clone.model.Folder;
import com.example.google_drive_clone.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
public class FolderController {

    @Autowired
    private FolderService folderService;

    // Create a new folder
    @PostMapping("/create")
    public ResponseEntity<Folder> createFolder(@RequestParam("folderName") String folderName) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Folder folder = folderService.createFolder(folderName, username);
            return ResponseEntity.ok(folder);
        } catch (Exception e) {
            System.err.println("Error in createFolder: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    // List all folders for the authenticated user
    @GetMapping
    public ResponseEntity<List<Folder>> listUserFolders() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            List<Folder> folders = folderService.getUserFolders(username);
            System.out.println("User: " + username + ", Folders: " + folders);
            if (folders == null || folders.isEmpty()) {
                System.out.println("No folders found for user: " + username);
            }
            return ResponseEntity.ok(folders);
        } catch (Exception e) {
            System.err.println("Error in listUserFolders: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

}
