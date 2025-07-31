package ro.mdx.meditation.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.mdx.meditation.dto.PackDto;
import ro.mdx.meditation.dto.SubjectDto;
import ro.mdx.meditation.services.PackService;
import ro.mdx.meditation.services.SubjectService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/settings")
@AllArgsConstructor
public class SettingsController {
    private final PackService packService;
    private final SubjectService subjectService;

    @GetMapping("/pack")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<PackDto>> getPacks() {
        return ResponseEntity.ok(packService.getAllPacks());
    }

    @PostMapping("/pack")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PackDto> savePack(@RequestBody PackDto packDto) {
        return ResponseEntity.ok(packService.savePack(packDto));
    }

    @DeleteMapping("/pack/{packId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> removePack(@PathVariable(name = "packId") UUID packId) {
        packService.deletePack(packId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/subject")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFESSOR')")
    public ResponseEntity<List<SubjectDto>> getSubjects() {
        return ResponseEntity.ok(subjectService.findAll());
    }

    @PostMapping("/subject")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<SubjectDto> saveSubject(@RequestBody SubjectDto subject) {
        return ResponseEntity.ok(subjectService.saveSubject(subject));
    }
}
