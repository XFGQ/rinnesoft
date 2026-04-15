package com.example.rinnesoft.controller;

import com.example.rinnesoft.model.Profile;
import com.example.rinnesoft.repository.ProfileRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    @GetMapping
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateProfile(
        @PathVariable Long id,
        @RequestBody Profile updatedProfile
    ) {
        Optional<Profile> existingProfile = profileRepository.findById(id);

        if (existingProfile.isPresent()) {
            Profile profile = existingProfile.get();
            profile.setUniversity(updatedProfile.getUniversity());
            profile.setFaculty(updatedProfile.getFaculty());
            profile.setBio(updatedProfile.getBio());
            profile.setEmail(updatedProfile.getEmail());
            profile.setGithubUrl(updatedProfile.getGithubUrl());
            profile.setLinkedinUrl(updatedProfile.getLinkedinUrl());
            // BirthYear gibi alanları da admin eklerse diye kaydediyoruz
            profile.setBirthYear(updatedProfile.getBirthYear());

            return ResponseEntity.ok(profileRepository.save(profile));
        }

        return ResponseEntity.notFound().build();
    }
}
