package com.example.google_drive_clone.repository;

import com.example.google_drive_clone.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    
    @Query("SELECT f FROM File f WHERE f.user.id = :ownerId")
    List<File> findByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT f FROM File f WHERE f.fileName LIKE %:keyword% AND f.user.id = :ownerId")
    List<File> searchFilesByKeyword(@Param("keyword") String keyword, @Param("ownerId") Long ownerId);
}
