package com.example.google_drive_clone.service;

import com.example.google_drive_clone.model.File;
import com.example.google_drive_clone.model.User;
import com.example.google_drive_clone.repository.FileRepository;
import com.example.google_drive_clone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void saveFile(MultipartFile file, String fileName, String username) throws Exception {
        try {
            User user = userRepository.findByUsername(username);
            System.out.println("Retrieved username: " + username);
            if (user == null) {
                throw new Exception("User not found");
            }

            File newFile = new File();
            newFile.setFileName(fileName);
            newFile.setContentType(file.getContentType());
            newFile.setData(file.getBytes());
            newFile.setUploadTime(LocalDateTime.now());
            newFile.setUser(user);

            fileRepository.save(newFile);
        } catch (Exception e) {
            System.err.println("Error in saveFile: " + e.getMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<File> getUserFiles(String username) throws Exception {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new Exception("User not found");
            }
            return fileRepository.findByOwnerId(user.getId());
        } catch (Exception e) {
            System.err.println("Error in getUserFiles: " + e.getMessage());
            throw e;
        }
    }
    @Transactional(readOnly = true)
    public File getFileById(Long id) throws Exception {
        try {
            return fileRepository.findById(id)
                    .orElseThrow(() -> new Exception("File not found"));
        } catch (Exception e) {
            System.err.println("Error in getFileById: " + e.getMessage());
            throw e;
        }
    }
}
