package com.example.rinnesoft.controller;

import com.example.rinnesoft.model.Skill;
import com.example.rinnesoft.repository.SkillRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// NOTE: No @CrossOrigin here — CORS is configured globally in SecurityConfig
@RestController
@RequestMapping("/api/skills")
public class SkillController {

    @Autowired
    private SkillRepository skillRepository;

    // ── PUBLIC ────────────────────────────────────────────────
    @GetMapping
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    // ── ADMIN ─────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<Skill> addSkill(@RequestBody Skill skill) {
        if (skill.getTitle() == null || skill.getTitle().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(skillRepository.save(skill));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Skill> updateSkill(
        @PathVariable Long id,
        @RequestBody Skill details
    ) {
        return skillRepository.findById(id)
            .map(s -> {
                if (details.getTitle() != null) s.setTitle(details.getTitle());
                if (details.getItems() != null) s.setItems(details.getItems());
                return ResponseEntity.ok(skillRepository.save(s));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        if (!skillRepository.existsById(id)) return ResponseEntity.notFound().build();
        skillRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
