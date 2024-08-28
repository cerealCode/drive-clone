package com.example.google_drive_clone.service;

import com.example.google_drive_clone.model.File;
import com.example.google_drive_clone.model.Folder;
import com.example.google_drive_clone.model.User;
import com.example.google_drive_clone.repository.FileRepository;
import com.example.google_drive_clone.repository.FolderRepository;
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
    private FolderRepository folderRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void saveFileToFolder(MultipartFile file, String fileName, Long folderId, String username) throws Exception {
        User user = userRepository.findByUsername(username);
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new Exception("Folder not found"));

        if (user == null) {
            throw new Exception("User not found");
        }

        File newFile = new File();
        newFile.setFileName(fileName);
        newFile.setContentType(file.getContentType());
        newFile.setData(file.getBytes());
        newFile.setUploadTime(LocalDateTime.now());
        newFile.setUser(user);
        newFile.setFolder(folder);

        fileRepository.save(newFile);
    }

    @Transactional(readOnly = true)
    public List<File> getFilesByFolderId(Long folderId, String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Exception("User not found");
        }
        return fileRepository.findByFolderIdAndUser_Id(folderId, user.getId()); 
    }


    @Transactional(readOnly = true)
    public File getFileById(Long id) throws Exception {
        return fileRepository.findById(id).orElseThrow(() -> new Exception("File not found"));
    }
}
