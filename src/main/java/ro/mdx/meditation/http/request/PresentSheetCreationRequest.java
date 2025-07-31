package ro.mdx.meditation.http.request;

import ro.mdx.meditation.dto.PresenceStudentDto;
import ro.mdx.meditation.dto.SubjectDto;

import java.time.LocalDateTime;
import java.util.List;

public record PresentSheetCreationRequest(
        LocalDateTime classDate,
        List<PresenceStudentDto> students,
        SubjectDto subject
) {
}
