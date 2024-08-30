package com.example.google_drive_clone.controller;

import com.example.google_drive_clone.model.Folder;
import com.example.google_drive_clone.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


import java.util.List;

@RestController
@RequestMapping("/api/folders")
public class FolderController {

    @Autowired
    private FolderService folderService;

    /**
     * Endpoint to create a new folder.
     * 
     * @param folderName The name of the folder to be created.
     * @return The created folder or an error response.
     */
    @PostMapping("/create")
    public ResponseEntity<Folder> createFolder(@RequestBody Map<String, String> payload) {
        try {
            // Correct key based on the JSON sent from the frontend
            String folderName = payload.get("name"); 
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Folder folder = folderService.createFolder(folderName, username);
            return ResponseEntity.ok(folder);
        } catch (Exception e) {
            System.err.println("Error in createFolder: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }


    /**
     * Endpoint to list all folders for the authenticated user.
     * 
     * @return A list of folders owned by the authenticated user.
     */
    @GetMapping
    public ResponseEntity<List<Folder>> listUserFolders() {
        try {
            // Get the currently authenticated user's username
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            
            // Fetch the folders from the service
            List<Folder> folders = folderService.getUserFolders(username);
            System.out.println("User: " + username + ", Folders: " + folders);
            
            if (folders == null || folders.isEmpty()) {
                System.out.println("No folders found for user: " + username);
            }
            
            // Return the list of folders
            return ResponseEntity.ok(folders);
        } catch (Exception e) {
            // Log and return an error response if any exception occurs
            System.err.println("Error in listUserFolders: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}
