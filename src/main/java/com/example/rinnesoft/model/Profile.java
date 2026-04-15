package com.example.rinnesoft.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String slug;
    private String role;
    private String university;
    private String faculty;
    private Integer birthYear;
    private String email;
    private String githubUrl;
    private String linkedinUrl;

    // Eksik olan ve hataya sebep olan satır:
    private String profileImageFilename;

    @Column(columnDefinition = "TEXT")
    private String bio;
}
