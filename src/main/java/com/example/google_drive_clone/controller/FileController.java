package com.example.google_drive_clone.controller;
import com.example.google_drive_clone.model.FileDTO;
import com.example.google_drive_clone.model.File;
import com.example.google_drive_clone.service.FileService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("fileName") String fileName) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            fileService.saveFile(file, fileName, username);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            System.err.println("Error in uploadFile: " + e.getMessage());
            return ResponseEntity.status(500).body("File upload failed");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileDTO>> listUserFiles() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            List<File> files = fileService.getUserFiles(username);
            List<FileDTO> fileDTOs = files.stream()
                    .map(file -> new FileDTO(file.getId(), file.getFileName()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(fileDTOs);
        } catch (Exception e) {
            System.err.println("Error in listUserFiles: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        try {
            File file = fileService.getFileById(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(file.getData());
        } catch (Exception e) {
            System.err.println("Error in downloadFile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
