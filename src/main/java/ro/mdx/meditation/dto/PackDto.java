package ro.mdx.meditation.dto;

public record PackDto(
        String id,
        String name,
        Integer numberOfSessions,
        Float price
) {
}
