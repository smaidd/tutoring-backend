package ro.mdx.meditation.dto;

import java.util.List;

public record StudentSheetDto(
        String id,
        StudentDto student,
        List<PresenceDto> presence
) {
}
