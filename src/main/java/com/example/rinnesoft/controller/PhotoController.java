package com.example.rinnesoft.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    // Dosyaların kaydedileceği klasör
    private final Path uploadDir = Paths.get("uploads");

    public PhotoController() {
        try {
            Files.createDirectories(uploadDir);
        } catch (Exception e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }

    @PostMapping("/{person}")
    public ResponseEntity<String> uploadPhoto(
        @PathVariable String person,
        @RequestParam("file") MultipartFile file
    ) {
        try {
            if (!person.equals("furkan") && !person.equals("alper")) {
                return ResponseEntity.badRequest().body("Invalid person");
            }
            // Gelen dosyayı person.jpg olarak kaydeder (örn: alper.jpg)
            Path filePath = uploadDir.resolve(person + ".jpg");
            Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
            );
            return ResponseEntity.ok("Photo Uploaded");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "Upload failed"
            );
        }
    }

    @GetMapping("/{person}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String person) {
        try {
            Path filePath = uploadDir.resolve(person + ".jpg");
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
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
}
