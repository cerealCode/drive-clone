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

    /**
     * Creates a new folder for the specified user.
     * 
     * @param folderName The name of the folder to be created.
     * @param username   The username of the owner.
     * @return The created folder.
     * @throws Exception if the user is not found.
     */
    @Transactional
    public Folder createFolder(String folderName, String username) throws Exception {
        // Retrieve the user by username
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            // Throw an exception if the user is not found
            throw new Exception("User not found");
        }
        
        // Create and save the new folder
        Folder folder = new Folder();
        folder.setName(folderName);
        folder.setOwner(user);
        return folderRepository.save(folder);
    }

    /**
     * Fetches all folders owned by the specified user.
     * 
     * @param username The username of the owner.
     * @return A list of folders owned by the user.
     * @throws Exception if the user is not found.
     */
    @Transactional(readOnly = true)
    public List<Folder> getUserFolders(String username) throws Exception {
        // Retrieve the user by username
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            // Throw an exception if the user is not found
            throw new Exception("User not found");
        }
        
        // Return the list of folders owned by the user
        return folderRepository.findByOwnerId(user.getId());
    }
}
