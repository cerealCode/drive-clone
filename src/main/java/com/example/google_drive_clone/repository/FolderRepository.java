package com.example.google_drive_clone.repository;

import com.example.google_drive_clone.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Query("SELECT f FROM Folder f WHERE f.owner.id = :ownerId")
    List<Folder> findByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT f FROM Folder f WHERE f.name LIKE %:keyword% AND f.owner.id = :ownerId")
    List<Folder> searchFoldersByKeyword(@Param("keyword") String keyword, @Param("ownerId") Long ownerId);
}
