package com.example.google_drive_clone.model;

public class FileDTO {
    private Long id;
    private String fileName;

    // Default constructor
    public FileDTO() {}

    // Constructor with parameters
    public FileDTO(Long id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
