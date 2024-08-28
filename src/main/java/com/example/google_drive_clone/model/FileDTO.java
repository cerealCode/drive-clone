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
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "FileDTO{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
