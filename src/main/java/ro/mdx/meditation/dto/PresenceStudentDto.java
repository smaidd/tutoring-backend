package ro.mdx.meditation.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PresenceStudentDto {
    private String id;
    private LocalDateTime classDate;
    private String subject;
    private String professor;
    private boolean present;
    private Integer delayMinutes;
    private Integer homeworkPercentage;
    private Integer participationPercentage;
    private String notes;
    private StudentDto student;
}
