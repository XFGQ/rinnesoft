package com.example.rinnesoft.model; // Burası tam olarak böyle olmalı

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String techIcon;

    @Column(columnDefinition = "TEXT")
    private String detail;

    private String techStack;
    private String category;
    private String githubUrl;
}
