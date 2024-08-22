package com.example.google_drive_clone.service;

import com.example.google_drive_clone.model.Folder;
import com.example.google_drive_clone.repository.FolderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FolderService {

    private final FolderRepository folderRepository;

    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    @Transactional
    public Folder save(Folder folder) {
        return folderRepository.save(folder);
    }

    @Transactional(readOnly = true)
    public List<Folder> getAllFolders() {
        return folderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Folder findById(Long folderId) {
        return folderRepository.findById(folderId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Folder> findFoldersByOwner(Long ownerId) {
        return folderRepository.findByOwnerId(ownerId);
    }

    @Transactional
    public void deleteFolder(Long id) {
        folderRepository.deleteById(id);
    }
}
