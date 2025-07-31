package ro.mdx.meditation.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.mdx.meditation.dto.PresenceDto;
import ro.mdx.meditation.dto.StudentSheetDto;
import ro.mdx.meditation.dto.StudentSheetStatistics;
import ro.mdx.meditation.http.request.PresentSheetCreationRequest;
import ro.mdx.meditation.model.ElementPage;
import ro.mdx.meditation.services.StudentSheetService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sheet")
@AllArgsConstructor
public class StudentSheetController {
    private final StudentSheetService studentSheetService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFESSOR')")
    public ResponseEntity<ElementPage<StudentSheetDto>> getStudentSheets(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return ResponseEntity.ok(studentSheetService.getStudentSheets(pageNumber, pageSize));
    }


    @GetMapping("/{studentId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFESSOR')")
    public ResponseEntity<StudentSheetDto> getStudentSheet(@PathVariable(name = "studentId") UUID studentId) {
        return ResponseEntity.ok(studentSheetService.getStudentSheet(studentId));
    }

    @PostMapping("/presence/{studentId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFESSOR')")
    public ResponseEntity<Void> savePresence(@RequestBody PresenceDto presence, @PathVariable(name = "studentId") UUID studentId) {
        studentSheetService.addNewStudentPresence(presence, studentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PostMapping("/presenceSheet")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFESSOR')")
    public ResponseEntity<Void> savePresenceSheet(@RequestBody PresentSheetCreationRequest presentSheetCreationRequest) {
        studentSheetService.createPresenceSheet(presentSheetCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/statistics/{sheetId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFESSOR')")
    public ResponseEntity<StudentSheetStatistics> getSheetStatistics(@PathVariable(name = "sheetId") UUID sheetId) {
        StudentSheetStatistics statistics = studentSheetService.getStudentSheetStatistics(sheetId);
        return ResponseEntity.ok(statistics);

    }
}
