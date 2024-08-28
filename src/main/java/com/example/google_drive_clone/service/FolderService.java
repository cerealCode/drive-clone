package com.example.google_drive_clone.service;

import com.example.google_drive_clone.model.Folder;
import com.example.google_drive_clone.model.User;
import com.example.google_drive_clone.repository.FolderRepository;
import com.example.google_drive_clone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Folder createFolder(String folderName, String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Exception("User not found");
        }
        Folder folder = new Folder();
        folder.setName(folderName);
        folder.setOwner(user);
        return folderRepository.save(folder);
    }

    @Transactional(readOnly = true)
    public List<Folder> getUserFolders(String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Exception("User not found");
        }
        return folderRepository.findByOwnerId(user.getId());
    }
}
