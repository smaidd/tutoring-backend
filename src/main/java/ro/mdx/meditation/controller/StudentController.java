package ro.mdx.meditation.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.mdx.meditation.dto.StudentDto;
import ro.mdx.meditation.http.StudentFilters;
import ro.mdx.meditation.model.ElementPage;
import ro.mdx.meditation.services.StudentService;
import ro.mdx.meditation.services.StudentSheetService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/student")
@AllArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final StudentSheetService studentSheetService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StudentDto> saveStudent(@RequestBody StudentDto studentDto) {
        StudentDto savedStudent = studentService.saveStudent(studentDto);
        studentSheetService.saveStudentSheet(savedStudent.id());
        return ResponseEntity.ok(savedStudent);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ElementPage<StudentDto>> getStudents(@RequestParam int pageNumber, @RequestParam int pageSize) {
        ElementPage<StudentDto> students = studentService.getStudents(pageNumber, pageSize);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{studentId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable(name = "studentId") UUID studentId) {
        StudentDto studentById = studentService.getStudentById(studentId);
        return ResponseEntity.ok(studentById);
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFESSOR')")
    public ResponseEntity<List<StudentDto>> getFilteredStudent(@RequestParam Optional<String> subject) {
        StudentFilters studentFilters = StudentFilters.builder().subject(subject)
                .build();
        List<StudentDto> students = studentService.getStudentsByFilters(studentFilters);
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{studentId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable(name = "studentId") UUID studentId, @RequestBody StudentDto studentDto) {
        StudentDto studentById = studentService.updateStudent(studentId, studentDto);
        return ResponseEntity.ok(studentById);
    }

    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable(name = "studentId") UUID studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
