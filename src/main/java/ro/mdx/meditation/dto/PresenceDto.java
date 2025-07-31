package ro.mdx.meditation.dto;

import java.time.LocalDateTime;

public record PresenceDto(
        String id,
        LocalDateTime classDate,
        SubjectDto subject,
        TeacherDto professor,
        boolean present,
        Integer delayMinutes,
        Integer homeworkPercentage,
        Integer participationPercentage,
        String notes) {
}
