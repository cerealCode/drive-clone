package com.example.google_drive_clone.service;

import com.example.google_drive_clone.model.File;
import com.example.google_drive_clone.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;

    
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public File save(File file) {
        return fileRepository.save(file);
    }

    @Transactional(readOnly = true)
    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    @Transactional(readOnly = true)
    public File findById(Long fileId) {
        return fileRepository.findById(fileId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<File> findFilesByOwner(Long ownerId) {
        return fileRepository.findByOwnerId(ownerId);
    }

    @Transactional
    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }
}
