package com.example.google_drive_clone.repository;

import com.example.google_drive_clone.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByOwnerId(Long ownerId);
}
