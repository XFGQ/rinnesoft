package com.example.rinnesoft.controller;

import com.example.rinnesoft.model.Project;
import com.example.rinnesoft.repository.ProjectRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        if (project.getTitle() == null || project.getTitle().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(projectRepository.save(project));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
        @PathVariable Long id,
        @RequestBody Project details
    ) {
        return projectRepository
            .findById(id)
            .map(p -> {
                if (details.getTitle() != null) p.setTitle(details.getTitle());
                if (details.getDescription() != null) p.setDescription(
                    details.getDescription()
                );
                if (details.getDetail() != null) p.setDetail(
                    details.getDetail()
                );
                if (details.getTechStack() != null) p.setTechStack(
                    details.getTechStack()
                );
                if (details.getCategory() != null) p.setCategory(
                    details.getCategory()
                );
                if (details.getGithubUrl() != null) p.setGithubUrl(
                    details.getGithubUrl()
                );

                // İŞTE EKSİK OLAN SATIR (Bunu eklemezsen ikon kaydedilmez!)
                if (details.getTechIcon() != null) p.setTechIcon(
                    details.getTechIcon()
                );

                return ResponseEntity.ok(projectRepository.save(p));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        if (
            !projectRepository.existsById(id)
        ) return ResponseEntity.notFound().build();
        projectRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
