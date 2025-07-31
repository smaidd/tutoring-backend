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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.mdx.meditation.dto.TeacherDto;
import ro.mdx.meditation.dto.TeacherSessionDto;
import ro.mdx.meditation.model.ElementPage;
import ro.mdx.meditation.services.TeacherService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teacher")
@AllArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<TeacherDto> createTeacher(@RequestBody TeacherDto teacherDto) {
        return ResponseEntity.ok(teacherService.createTeacher(teacherDto));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ElementPage<TeacherDto>> getProfessors(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return ResponseEntity.ok(teacherService.getTeachers(pageNumber, pageSize));
    }

    @GetMapping("{teacherId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<TeacherDto> getTeacher(@PathVariable UUID teacherId) {
        return ResponseEntity.ok(teacherService.getTeacher(teacherId));
    }

    @GetMapping("sheet/{teacherId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<TeacherSessionDto>> getTeacherSheet(@PathVariable UUID teacherId, @RequestParam String from, @RequestParam String to) {
        return ResponseEntity.ok(teacherService.getTeacherClasses(teacherId, from, to));
    }

    @DeleteMapping("/{teacherId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteTeacher(@PathVariable(name = "teacherId") UUID teacherId) {
        teacherService.deleteTeacher(teacherId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
