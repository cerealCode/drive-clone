package com.example.google_drive_clone.service;

import com.example.google_drive_clone.model.File;
import com.example.google_drive_clone.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    public File saveFile(File file) {
        return fileRepository.save(file);
    }

    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }
}
