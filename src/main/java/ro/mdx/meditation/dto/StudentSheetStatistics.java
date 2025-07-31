package ro.mdx.meditation.dto;

public record StudentSheetStatistics(
        int totalNumberOfSessions,
        double presencePercentage,
        double medianActiveParticipation,
        double medianHomework
) {
}
