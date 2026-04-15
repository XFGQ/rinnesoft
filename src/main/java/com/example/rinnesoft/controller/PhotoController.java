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
        @RequestParam("file") MultipartFile file) {
    try {
        if (!person.equals("furkan") && !person.equals("alper")) {
            return ResponseEntity.badRequest().body("Invalid person");
        }

        // Gelen dosyanın orijinal adını ve uzantısını al (Örn: .png veya .jpg)
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }

        // Önce eski olabilecek tüm formatları (jpg, jpeg, png) temizleyelim ki çakışmasın
        Files.deleteIfExists(uploadDir.resolve(person + ".jpg"));
        Files.deleteIfExists(uploadDir.resolve(person + ".jpeg"));
        Files.deleteIfExists(uploadDir.resolve(person + ".png"));

        // Yeni dosyayı uygun uzantıyla kaydet
        Path filePath = uploadDir.resolve(person + extension);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return ResponseEntity.ok("Photo Uploaded");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
    }
}

@GetMapping("/{person}")
public ResponseEntity<Resource> getPhoto(@PathVariable String person) {
    try {
        // Sırasıyla dosyayı ara
        Path filePath = null;
        String[] extensions = {".png", ".jpg", ".jpeg"};

        for (String ext : extensions) {
            Path p = uploadDir.resolve(person + ext);
            if (Files.exists(p)) {
                filePath = p;
                break;
            }
        }

        if (filePath != null) {
            Resource resource = new UrlResource(filePath.toUri());
            // Dosya türünü otomatik tespit et (image/png veya image/jpeg)
            String contentType = Files.probeContentType(filePath);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        }
        return ResponseEntity.notFound().build();
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

}
