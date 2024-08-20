package com.example.google_drive_clone.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users") // Add a table name if you want to specify a name for the table in the DB
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files;

    // Default constructor
    public User() {}

    // Parameterized constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter for id
    public Long getId() {
        return id;
    }

    // Setter for id
    public void setId(Long id) {
        this.id = id;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for files
    public List<File> getFiles() {
        return files;
    }

    // Setter for files
    public void setFiles(List<File> files) {
        this.files = files;
    }
}
