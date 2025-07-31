package ro.mdx.meditation.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TeacherSessionDto(
        String id,
        TeacherDto teacher,
        List<StudentDto> students,
        Double remuneration,
        SubjectDto subject,
        LocalDateTime classDate
) {
}
