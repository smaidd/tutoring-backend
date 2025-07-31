package ro.mdx.meditation.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link ro.mdx.meditation.model.Student}
 */
public record StudentDto(
        String id,
        String name,
        String phoneNumber,
        String studentClass,
        String profile,
        List<SubjectDto> subjects,
        String observations,
        String parentName,
        String parentPhoneNumber,
        String parentEmail,
        PackDto pack,
        Instant firstAppointment,
        Instant dateOfBirth,
        Boolean contacted,
        Boolean confirmed,
        Integer numberOfSessions
) implements Serializable {
}