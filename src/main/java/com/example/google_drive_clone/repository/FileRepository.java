package com.example.google_drive_clone.repository;

import com.example.google_drive_clone.model.File;
import com.example.google_drive_clone.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    // Find files by folder and user ID (owner)
    List<File> findByFolderIdAndUser_Id(Long folderId, Long userId);

    // Find files by owner ID
    @Query("SELECT f FROM File f WHERE f.user.id = :ownerId")
    List<File> findByOwnerId(@Param("ownerId") Long ownerId);

    // Search files by keyword and owner ID
    @Query("SELECT f FROM File f WHERE f.fileName LIKE %:keyword% AND f.user.id = :ownerId")
    List<File> searchFilesByKeyword(@Param("keyword") String keyword, @Param("ownerId") Long ownerId);

    // Find files by folder and owner ID
    @Query("SELECT f FROM File f WHERE f.folder = :folder AND f.user.id = :ownerId")
    List<File> findByFolderAndOwnerId(@Param("folder") Folder folder, @Param("ownerId") Long ownerId);

    // Search files by keyword within a specific folder and owner ID
    @Query("SELECT f FROM File f WHERE f.fileName LIKE %:keyword% AND f.folder = :folder AND f.user.id = :ownerId")
    List<File> searchFilesByKeywordAndFolder(@Param("keyword") String keyword, @Param("folder") Folder folder, @Param("ownerId") Long ownerId);

    // Find files by folder and user ID using another query
    @Query("SELECT f FROM File f WHERE f.user.id = :userId AND f.folder.id = :folderId")
    List<File> findByFolderIdAndUserId(@Param("folderId") Long folderId, @Param("userId") Long userId);
}
