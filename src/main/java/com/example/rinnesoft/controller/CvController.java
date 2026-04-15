package com.example.rinnesoft.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/cv")
public class CvController {

    private final Path uploadDir = Paths.get("uploads");

    public CvController() {
        try {
            Files.createDirectories(uploadDir);
        } catch (Exception e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }

    @PostMapping("/{person}")
    public ResponseEntity<String> uploadCv(
        @PathVariable String person,
        @RequestParam("file") MultipartFile file
    ) {
        try {
            if (!person.equals("furkan") && !person.equals("alper")) {
                return ResponseEntity.badRequest().body("Invalid person");
            }
            Path filePath = uploadDir.resolve(person + ".pdf");
            Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
            );
            return ResponseEntity.ok("CV Uploaded");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "Upload failed"
            );
        }
    }

    @GetMapping("/{person}")
    public ResponseEntity<Resource> getCv(@PathVariable String person) {
        try {
            Path filePath = uploadDir.resolve(person + ".pdf");
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                    .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + person + "_CV.pdf\""
                    )
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    @DeleteMapping("/{person}")
    public ResponseEntity<String> deleteCv(@PathVariable String person) {
        try {
            Path filePath = uploadDir.resolve(person + ".pdf");
            Files.deleteIfExists(filePath);
            return ResponseEntity.ok("CV Deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "Delete failed"
            );
        }
    }
}
